package core.supervisor;

public interface TaskListener {

	void onTaskReceived( String newTaskXML );
	
}
