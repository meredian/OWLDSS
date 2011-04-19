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
import core.owl.OWLOntologyShell;
import core.owl.objects.Task;
import core.utils.IndividualXMLParser;
import core.utils.MethodSelectionMode;

public class TaskProcessor implements TaskListener {

	private Queue< String > taskQueue = new LinkedList< String >();
	private boolean canceled = false;
	
	private final String ontologyPath;
	private final String ontologyIRI;
	private final MethodSelectionMode methodSelectionMode;

	public TaskProcessor(String ontologyPath, String ontologyIRI, MethodSelectionMode methodSelectionMode) {
		this.ontologyPath = ontologyPath;
		this.ontologyIRI = ontologyIRI;
		this.methodSelectionMode = methodSelectionMode; 
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
		do {
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
				OWLOntologyShell taskContext = new OWLOntologyShell(ontologyManager, this.ontologyIRI);
				ImportManager importManager = new ImportManager(taskContext);
				SolverManager solverManager = new SolverManager(taskContext, importManager, methodSelectionMode);
				RenderManager renderManager = new RenderManager(methodSelectionMode);

				// Put the initial task into the task context
				IndividualXMLParser taskParser = new IndividualXMLParser(taskContext, taskXML);
				Task currentTask = this.getNextTask(taskContext);
				while (true) {
					// Import the data needed for the chosen solving method
					// ...and run the chosen solving method
					solverManager.process(currentTask);

					// Mark the [sub]task object as solved
					currentTask.markAsSolved();

					this.bindNewSubtasksToTree(currentTask);

					// Select next subtask
					Task nextTask = this.getNextTask(taskContext);
					if (nextTask != null)
						currentTask = nextTask;
					else
						break;
					
					taskContext.dumpOntology();
				}
				
				renderManager.process( currentTask.getResult() );
				taskContext.dumpOntology();
			} catch( Exception e ) {
				System.err.println( "Task solution failed! Stack trace follows..." );
				e.printStackTrace();
			}
			
		} while (!this.canceled);
	}
	
	public void cancel() {
		this.canceled = true;
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
	
	private Task getNextTask(OWLOntologyShell taskContext) throws Exception {
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

			if (subtasksSolved) {
				task.collectSubtasksResults();
				return task;
			}
		}

		return null;
	}

}
