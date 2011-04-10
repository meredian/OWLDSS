package core.supervisor;

import core.owl.objects.Task;


public interface TaskListener {

	void onTaskReceived( Task newTask );
	
}
