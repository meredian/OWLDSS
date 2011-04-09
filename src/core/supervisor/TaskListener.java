package core.supervisor;


public interface TaskListener {

	void onTaskReceived( Task newTask );
	
}
