package core.supervisor;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import core.managers.ImportManager;
import core.managers.RenderManager;
import core.managers.SolverManager;
import core.owl.OWLOntologyObjectShell;
import core.owl.base.OWLIndividualObject;
import core.owl.objects.Task;

public class TaskProcessor implements TaskListener {

	private Queue< Task > taskQueue = new LinkedList< Task >();
	private boolean canceled = false;
	
	private static final String ONTOLOGY_PATH = "file:/c:/main.owl";
	
	@Override
	/**
	 * Inserts a new task into queue.
	 */
	public void onTaskReceived( Task newTask ) {
		taskQueue.add( newTask );
	}
	
	void process() {
		
		Task nextTask;
		while( ! this.canceled ) {
			// Obtain initial task from the outside 
			nextTask = taskQueue.poll();
			if( nextTask == null ) {
				try {
					Thread.sleep( 50 ); // sleep portions of 50 ms until a task is obtained
				} catch( InterruptedException ignored ) {} 
				continue;
			}
			
			try {
				// Generate new task context
				OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
				ontologyManager.loadOntologyFromOntologyDocument( new File( ONTOLOGY_PATH ) );

				// Initialize some services
				OWLOntologyObjectShell taskContext = new OWLOntologyObjectShell( ontologyManager, "http://www.iis.nsk.su/ontologies/main" );
				ImportManager importManager = new ImportManager(taskContext); 
				SolverManager solverManager = new SolverManager(importManager);
				RenderManager renderManager = new RenderManager(); 
            
				// Put the initial task into the task context
				OWLIndividualObject initialTask = nextTask.putInto( taskContext );
				assert( initialTask != null );
				Task nextTaskObject; 
				do {
					// Select next subtask
					nextTaskObject = this.selectNextTaskObject( taskContext );
					
					// Import the data needed for the chosen solving method
					// 	  ...and run the chosen solving method
					solverManager.process(nextTaskObject);
					
					// Mark the [sub]task object as solved
					nextTaskObject.setStatus( Task.Status.SOLVED );
					
					this.bindNewSubtasksToTree(nextTaskObject); 
				} while( nextTaskObject != null );
				
				renderManager.process( nextTask.getResult() );
				
				// Run the chosen post method 
				// postServiceManager.runPost( initialTask );
			} catch( Exception e ) {
				System.err.println( "Task solution failed! Stack trace follows..." );
				e.printStackTrace();
			}
			
		}
	}
	
	private void bindNewSubtasksToTree(Task task) throws Exception {
		Task superTask = task.getSuperTask();
		if (superTask == null)
			return;
		Set<Task> outputTasks = task.getOutputTasks();
		for (Task outputTask: outputTasks)
			if (outputTask.getSuperTask() == null) {
				superTask.addSubTask( outputTask );
				return;
			}
		
		if (!outputTasks.isEmpty())
			throw new Exception("Could not bind new subtasks to existing tree");
	}
	
	private Task selectNextTaskObject(OWLOntologyObjectShell taskContext) throws Exception {
		Set<Task> allTasks = taskContext.getTasks();

		for (Task task : allTasks) {
			if (task.getStatus() == Task.Status.SOLVED)
				continue;

			Set<Task> subTasks = task.getSubTasks();
			boolean subtasksSolved = true;
			for (Task subTask : subTasks)
				if (subTask.getStatus() == Task.Status.QUEUED) {
					subtasksSolved = false;
					break;
				}

			if (subtasksSolved)
				return task;
		}

		return null;
	}

}
