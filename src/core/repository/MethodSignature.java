package core.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodSignature {

	private final String name;
	private MethodStatus status = MethodStatus.STATUS_UNKNOWN;
	private Map<String, String> params = null;
	private Map<String, String> options = null;

	public MethodSignature(String methodName) {
		if (methodName == null)
			throw new NullPointerException("solverName is null");
		this.name = methodName;
	}

	public String getName() {
		return name;
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

	public void setOptions(String key, String value) {
		if (options == null) {
			options = new HashMap<String, String>();
		}
		options.put(key, value);
	}

	public String getOptions(String key) {
		if (options == null) {
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

	public MethodStatus getStatus() {
		return this.status;
	}

	public void setStatus(MethodStatus newStatus) {
		this.status = newStatus;
	}
}
