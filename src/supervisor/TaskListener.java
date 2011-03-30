package supervisor;


public interface TaskListener {

	void onTaskReceived( Task newTask );
	
}
