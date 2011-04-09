package core.repositary;

import java.util.HashMap;
import java.util.Map;

public class SolverSignature {
	
	private final String name;
	private final TaskSignature signature;
	private Map<String, String> additionalMethodInfo = null;

	public SolverSignature(String solverName, TaskSignature taskSignature) {
		this.name = solverName;
		this.signature = taskSignature;
	}
	
	public TaskSignature getTaskSignature() {
		return signature;
	}

	public String getName() {
		return name;
	}
	
	public boolean solves( TaskSignature taskSignature ) {
		if ( this.signature.compareTo(taskSignature) == 0 ) { 
			return true;
		} else {
			return false;
		}
	}
	
	public void setAdditionalInfo(String key, String value) {
		if ( additionalMethodInfo == null ) {
			additionalMethodInfo = new HashMap<String, String>();
		}
		additionalMethodInfo.put(key, value);
	}
	
	public String getAdditionalInfo(String key) {
		if ( additionalMethodInfo == null ) {
			return null;
		}
		return additionalMethodInfo.get(key);
	}
	
}
