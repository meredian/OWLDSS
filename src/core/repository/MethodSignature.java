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

	public Map<String,String> getOptions() {
		return options;
	}

	public Map<String,String> getParams() {
		return params;
	}

	public boolean ensureParams(List<String> keys) {
		for (String key : keys) {
			if (!params.containsKey(key)) {
				return false;
			}
		}
		return true;
	}

	public Map<String, String> assertOptions(Map<String, String> curentOptions) {
		HashMap<String, String> actualOptions = new HashMap<String, String>(this.options);
		for (String key : this.options.keySet()) {
			if (curentOptions.containsKey(key)) {
				actualOptions.put(key, curentOptions.get(key));
			}
		}
		return actualOptions;
	}

	public MethodSignature cloneWithOptions(Map<String, String> curentOptions) {
		MethodSignature asserted = new MethodSignature(this.name);
		if( this.params != null) {
			asserted.params = new HashMap<String, String>(this.params);
		}
		if (this.options != null) {
			asserted.options = assertOptions(curentOptions);
		}
		asserted.status = this.status;
		return asserted;
	}
}
