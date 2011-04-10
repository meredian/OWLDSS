package core.supervisor;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import core.managers.SolverManager;
import core.owl.OWLOntologyObjectShell;
import core.owl.base.OWLClassObject;
import core.owl.base.OWLIndividualObject;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;

public class TaskProcessor implements TaskListener {

	private Queue< Task > taskQueue = new LinkedList< Task >();
	private boolean canceled = false;
	
	private static final String ONTOLOGY_PATH = "file:/c:/main.owl";
	public static final String ABSTRACT_TASK_CLASS = "AbstractTask";
	public static final String TASK_PRIORITY_ATTR = "TaskPriority";
	public static final String TASK_INDEX_ATTR = "TaskIndex";
	public static final String TASK_SOLVED_ATTR = "TaskSolved";
	
	@Override
	/**
	 * Inserts a new task into queue.
	 */
	public void onTaskReceived( Task newTask ) {
		taskQueue.add( newTask );
	}
	
	void defineTaskIndexes( OWLOntologyObjectShell taskContext, OWLIndividualObject parentingTaskObject ) throws Exception {
		Collection< OWLIndividualObject > allTasks = taskContext.getClassObject( ABSTRACT_TASK_CLASS ).getIndividuals( false );
		int maxPriority = 0;
		
		/** 
		 * The algorithm follows:
		 * 1) Every task should have attribute TASK_PRIORITY_ATTR defined. Find maximum of them.
		 * 2) Iterate through all existing tasks with undefined TASK_INDEX_ATTR. Define it as:
		 *    (index of parenting task).000(priority of sub task)
		 *    Zeros are added to get the same subtask index length for all subtasks of the current task.
		 *    If a task does not have TASK_PRIORITY_ATTR attribute, add it by increasing maxPriority.
		 **/
		
		for( OWLIndividualObject task: allTasks ) {
			Integer taskPriority = task.getPropertyByName( TASK_PRIORITY_ATTR ).getIntegerValue();
			if( taskPriority != null )
				if( taskPriority >= maxPriority )
					maxPriority = taskPriority;
		}
		
		int digits = String.valueOf( maxPriority ).length();
		String baseIndex = parentingTaskObject.getPropertyByName( TASK_INDEX_ATTR ).getStringValue() + ".";
		
		for( OWLIndividualObject task: allTasks ) {
			Integer taskPriority = task.getPropertyByName( TASK_PRIORITY_ATTR ).getIntegerValue();
			
			if( taskPriority == null ) {
				taskPriority = ++maxPriority;
				task.getPropertyByName( TASK_PRIORITY_ATTR ).setIntegerValue( taskPriority );
			}

			assert( taskPriority != null );
			
			String taskIndex = String.valueOf( taskPriority );
			while( taskIndex.length() < digits )
				taskIndex = "0" + taskIndex;
			
			task.getPropertyByName( TASK_INDEX_ATTR ).setStringValue( baseIndex + taskIndex );
		}
	}
	
	void process() {
		
		Task nextTask;
		while( ! this.canceled ) {
			// 1. Obtain initial task from the outside 
			nextTask = taskQueue.poll();
			if( nextTask == null ) {
				try {
					Thread.sleep( 50 ); // sleep portions of 50 ms until a task is obtained
				} catch( InterruptedException ignored ) {} 
				continue;
			}
			
			try {
				// 2. Generate new task context
				OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
				ontologyManager.loadOntologyFromOntologyDocument( new File( ONTOLOGY_PATH ) );

				// 2a. Initialize some services
				OWLOntologyObjectShell taskContext = new OWLOntologyObjectShell( ontologyManager, "http://www.iis.nsk.su/ontologies/main" );
				SolverManager solverManager = new SolverManager( taskContext );
				// QueryServiceManager queryServiceManager = new QueryServiceManager();
				// PostServiceManager postServiceManager = new PostServiceManager();
            
				// 3. Put the initial task into the task context
				OWLIndividualObject initialTask = nextTask.putInto( taskContext );
				assert( initialTask != null );
				Task nextTaskObject; 
				do {
					// 8. Select next subtask
					this.defineTaskIndexes( taskContext, null );
					nextTaskObject = this.selectNextTaskObject( taskContext );
					
					// 4. Choose solving method
					// ?
					SolvingMethod solvingMethod = null;
			
					// 5. Import the data needed for the chosen solving method
					// queryServiceManager.runQuery( nextTaskObject );
					
					// 6. Run the chosen solving method
					solverManager.runSolver( solvingMethod, nextTaskObject );
					
					// 7. Mark the [sub]task object as solved
					nextTaskObject.setStatus( Task.Status.SOLVED );
				} while( nextTaskObject != null );
				
				// 9. Choose post method
				// ?
				
				// 10. Run the chosen post method 
				// postServiceManager.runPost( initialTask );
			} catch( Exception e ) {
				System.err.println( "Task solution failed! Stack trace follows..." );
				e.printStackTrace();
			}
			
		}
	}


	private Task selectNextTaskObject( OWLOntologyObjectShell taskContext ) throws Exception {
		OWLClassObject abstractTaskClassObject = taskContext.getClassObject( ABSTRACT_TASK_CLASS );
		Collection< OWLIndividualObject > allTasks = abstractTaskClassObject.getIndividuals( false );
		
		String minIndex = new String();
		OWLIndividualObject chosenTask = null;
		
		for( OWLIndividualObject task: allTasks ) {
			Boolean taskSolved = task.getPropertyByName( TASK_SOLVED_ATTR ).getBooleanValue();
			assert( taskSolved != null );
			if( taskSolved.booleanValue() )
				continue;
			
			String taskIndex = task.getPropertyByName( TASK_INDEX_ATTR ).getStringValue();
			assert( taskIndex != null );
			
			if( minIndex.compareTo( taskIndex ) > 0 ) {
				minIndex = taskIndex;
				chosenTask = task;
			}
		}
		
		return null; // TODO chosenTask;
	}
	
}
