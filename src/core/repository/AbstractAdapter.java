package core.repository;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLIndividual;

import core.interfaces.Adapter;

public abstract class AbstractAdapter implements Adapter {
	
	protected List<SolverSignature> solvers;
	protected List<String> mandatoryParams;
	protected List<String> optionalParams;
	
	@Override
	public List<SolverSignature> getSolvers() {
		return solvers;
	}

	@Override
	public List<SolverSignature> getSolversByTask(TaskSignature task) {
		List<SolverSignature> filteredByTask = new ArrayList<SolverSignature>();
		for (SolverSignature solver : solvers) {
			if(solver.solves(task)) {
				filteredByTask.add(solver);
			}
		}
		return filteredByTask.size() > 0 ? filteredByTask : null;
	}

	@Override
	public boolean solveTask(OWLIndividual task) {
		List<SolverSignature> filteredByTask = getSolversByTask( new TaskSignature(task));
		if ( filteredByTask.size() > 0) {
			return solveTaskBySolver(task, filteredByTask.get(0));
		} else {
			return false;
		}
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
	public boolean addSolver(SolverSignature solver) {
		if (solver.assertParams(mandatoryParams) && !solvers.contains(solver)) {
			solvers.add(solver);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeSolver(SolverSignature solver) {
		return solvers.remove(solver);
	}

	@Override
	public List<SolverSignature> testSolvers() {
		for (SolverSignature solver : solvers) {
			solver = testSolver(solver);
		}
		return this.getSolvers();
	}

	@Override
	public SolverSignature testSolver(SolverSignature solver) {
		if ( solver != null ) { 
			solver.setStatus(callTest(solver));
			return solver;
		} else {
			return null;
		}
	}
	
	protected abstract int callTest(SolverSignature solver);
}
