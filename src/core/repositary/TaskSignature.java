package core.repositary;

import java.util.ArrayList;
import java.util.List;

public class TaskSignature implements Comparable<TaskSignature> {

	private static final int COMPARE_DIFF_TASK_NAMES = -1;
	private static final int COMPARE_DIFF_PARAMS_LENGTH = 1;
	private static final int COMPARE_DIFF_PARAMS_NAMES = 2;

	private final String taskName;
	private List<String> expectedParamsByClass = new ArrayList<String>();

	public TaskSignature(String taskName) {
		if (taskName == null)
			throw new NullPointerException("taskName is null");
		this.taskName = taskName;
	}

	public void addExpectedParam(String className) {
		expectedParamsByClass.add(className);
	}

	public String getTaskName() {
		return taskName;
	}

	public List<String> getParams() {
		return expectedParamsByClass;
	}

	@Override
	public int compareTo(TaskSignature o) {
		int compareName = this.taskName.compareTo(o.getTaskName());
		if (compareName != 0) {
			return COMPARE_DIFF_TASK_NAMES;
		} else {
			if (this.expectedParamsByClass.size() != o.getParams().size()) {
				return COMPARE_DIFF_PARAMS_LENGTH;
			} else {
				for (int i = 0; i < this.expectedParamsByClass.size(); ++i) {
					if (this.expectedParamsByClass.get(i).compareTo(
							o.getParams().get(i)) != 0) {
						return COMPARE_DIFF_PARAMS_NAMES;
					}
				}
				return 0;
			}
		}
	}

}
