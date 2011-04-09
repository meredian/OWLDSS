package core.repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolverSignature implements Serializable {

	private static final long serialVersionUID = -7320743339154303279L;

	private final String name;
	private final TaskSignature signature;
	private SolverStatus status = SolverStatus.STATUS_UNKNOWN;
	private Map<String, String> params = null;

	public SolverSignature(String solverName, TaskSignature taskSignature) {
		if (solverName == null)
			throw new NullPointerException("solverName is null");
		if (taskSignature == null)
			throw new NullPointerException("taskSignature is null");
		this.name = solverName;
		this.signature = taskSignature;
	}

	public TaskSignature getTaskSignature() {
		return signature;
	}

	public String getName() {
		return name;
	}

	public boolean solves(TaskSignature taskSignature) {
		if (this.signature.compareTo(taskSignature) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setParams(String key, String value) {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(key, value);
	}

	public String getParams(String key) {
		if (params == null) {
			return null;
		}
		return params.get(key);
	}

	public boolean assertParams( List<String> keys ) {
		for (String key : keys) {
			if( !params.containsKey(key) ) {
				return false;
			}
		}
		return true;
	}

	public SolverStatus getStatus() {
		return this.status;
	}

	public void setStatus(SolverStatus newStatus) {
		this.status = newStatus;
	}
}
