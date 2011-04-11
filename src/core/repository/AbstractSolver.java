package core.repository;

import java.util.List;

import core.interfaces.Solver;
import core.owl.objects.SolvingMethod;

public abstract class AbstractSolver implements Solver {

	protected List<MethodSignature> methods;
	protected List<String> mandatoryParams;
	protected List<String> optionalParams;

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
		MethodSignature method = this.getMethodByName(solvingMethod.getMethodName());
		//method = method.cloneWithOptions(solvingMethod.getParameters());
		return method;
	}

	@Override
	public List<String> getMandatoryParams() {
		return mandatoryParams;
	}

	@Override
	public List<String> getOptionalParams() {
		return optionalParams;
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

	protected abstract MethodStatus callTest(MethodSignature method);
}