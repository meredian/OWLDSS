package core.supervisor;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import core.managers.ImportManager;
import core.managers.RenderManager;
import core.managers.SolverManager;
import core.owl.OWLOntologyObjectShell;
import core.owl.objects.Task;

public class TaskProcessor implements TaskListener {

	private Queue< String > taskQueue = new LinkedList< String >();
	private boolean canceled = false;
	
	private final String ontologyPath;
	private final String ontologyIRI;
	
	public TaskProcessor(String ontologyPath, String ontologyIRI) {
		this.ontologyPath = ontologyPath;
		this.ontologyIRI = ontologyIRI;
	}
	
	@Override
	/**
	 * Inserts a new task into queue.
	 */
	public void onTaskReceived( String newTaskXML ) {
		taskQueue.add( newTaskXML );
	}
	
	public void process() {
		String taskXML;
		while( ! this.canceled ) {
			// Obtain initial task from the outside 
			taskXML = taskQueue.poll();
			if( taskXML == null ) {
				try {
					Thread.sleep( 50 ); // sleep portions of 50 ms until a task is obtained
				} catch( InterruptedException ignored ) {} 
				continue;
			}
			
			try {
				// Generate new task context
				OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
				ontologyManager.loadOntologyFromOntologyDocument(new File(ontologyPath));

				// Initialize some services
				OWLOntologyObjectShell taskContext = new OWLOntologyObjectShell(ontologyManager, this.ontologyIRI);
				ImportManager importManager = new ImportManager(taskContext);
				SolverManager solverManager = new SolverManager(taskContext, importManager);
				RenderManager renderManager = new RenderManager();

				// Put the initial task into the task context
				taskContext.createIndividualsFromXML(taskXML);
				taskContext.dumpOntology();
				Task currentTask = this.selectNextTaskObject(taskContext);
				while (true) {
					// Import the data needed for the chosen solving method
					// ...and run the chosen solving method
					solverManager.process(currentTask);

					// Mark the [sub]task object as solved
					currentTask.markAsSolved();

					this.bindNewSubtasksToTree(currentTask);

					// Select next subtask
					Task nextTask = this.selectNextTaskObject(taskContext);
					if (nextTask != null)
						currentTask = nextTask;
					else
						break;
				}
				
				renderManager.process( currentTask.getResult() );
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
