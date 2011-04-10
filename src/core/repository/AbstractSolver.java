package core.repository;

import java.util.List;

import core.interfaces.Solver;

public abstract class AbstractSolver implements Solver {

	protected List<MethodSignature> solvers;
	protected List<String> mandatoryParams;
	protected List<String> optionalParams;

	@Override
	public List<MethodSignature> getMethods() {
		return solvers;
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
	public boolean addSolver(MethodSignature solver) {
		if (solver.assertParams(mandatoryParams) && !solvers.contains(solver)) {
			solvers.add(solver);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeSolver(MethodSignature solver) {
		return solvers.remove(solver);
	}

	@Override
	public List<MethodSignature> testMethods() {
		for (MethodSignature solver : solvers) {
			testMethod(solver);
		}
		return this.getMethods();
	}

	@Override
	public MethodSignature testMethod(MethodSignature solver) {
		if ( solver != null ) {
			solver.setStatus(callTest(solver));
			return solver;
		} else {
			return null;
		}
	}

	protected abstract MethodStatus callTest(MethodSignature solver);
}
