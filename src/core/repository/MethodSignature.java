package core.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodSignature {

	private final String name;
	private MethodStatus status = MethodStatus.UNKNOWN;
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
	
	public MethodStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(MethodStatus newStatus) {
		this.status = newStatus;
	}

	public void setParam(String key, String value) {
		if (params == null) {
			params = new HashMap<String, String>();
		}
		params.put(key, value);
	}

	public String getParam(String key) {
		if (params == null) {
			return null;
		}
		return params.get(key);
	}

	public void setOption(String key, String value) {
		if (options == null) {
			options = new HashMap<String, String>();
		}
		options.put(key, value);
	}

	public String getOption(String key) {
		if (options == null) {
			return null;
		}
		return params.get(key);
	}
	
	public boolean ensureParams( List<String> keys ) {
		for (String key : keys) {
			if( !params.containsKey(key) ) {
				return false;
			}
		}
		return true;
	}
	
	public Map<String, String> assertOptions(Map<String, String> currentOptions) {
		HashMap<String, String> actualOptions = new HashMap<String, String>(this.options);
		for (String key : options.keySet()) {
			if( currentOptions.containsKey(key) ) {
				actualOptions.put(key, currentOptions.get(key));
			}
		}
		return actualOptions;
	}
	
	public MethodSignature cloneWithOptions(Map<String, String> currentOptions) {
		MethodSignature asserted = new MethodSignature(this.name);
		asserted.params = new HashMap<String, String>(this.options);
		asserted.status = this.status;
		asserted.options = assertOptions(currentOptions);
		return asserted;
		
	}
}
