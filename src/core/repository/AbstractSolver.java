package core.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.interfaces.Solver;
import core.owl.objects.SolvingMethod;

public abstract class AbstractSolver implements Solver {

	protected List<MethodSignature> methods;
	protected List<String> mandatoryParams;
	protected Map<String, String> options;

	protected void init() {
		methods = new ArrayList<MethodSignature>();
		mandatoryParams = new ArrayList<String>();
		options = new HashMap<String, String>();
	}
	
	@Override
	public List<MethodSignature> getMethods() {
		return methods;
	}

	@Override
	public MethodSignature getMethodByName(String name) {
		for (MethodSignature method : methods) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		return null;
	}

	@Override
	public MethodSignature getMethodBySolvingMethod(SolvingMethod solvingMethod) {
		// TODO: Use normal data-properties instead of new HashMap<>()
		MethodSignature method = this.getMethodByName(solvingMethod.getMethodName());
		return method.cloneWithOptions(new HashMap<String, String>());
	}

	@Override
	public List<String> getMandatoryParams() {
		return mandatoryParams;
	}

	@Override
	public Map<String, String> getOptions() {
		return options;
	}

	@Override
	public boolean addMethod(MethodSignature method) {
		if (method.ensureParams(mandatoryParams) && !methods.contains(method)) {
			methods.add(method);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeMethod(MethodSignature method) {
		return methods.remove(method);
	}

	@Override
	public List<MethodSignature> testMethods() {
		for (MethodSignature method : methods) {
			testMethod(method);
		}
		return this.getMethods();
	}

	@Override
	public MethodSignature testMethod(MethodSignature method) {
		if (method != null) {
			method.setStatus(callTest(method));
			return method;
		} else {
			return null;
		}
	}

	protected MethodStatus callTest(MethodSignature method) {
		return MethodStatus.UNKNOWN;
	}
}
