package core.repository;

import java.util.LinkedList;
import java.util.List;

import core.interfaces.Solver;
import core.owl.objects.SolvingMethod;

public class SolverRepository {

	private List<Solver> solvers = new LinkedList<Solver>();

	public SolverRepository() {
		// TODO Auto-generated constructor stub
	}

	public Solver getSolver(SolvingMethod solvingMethod) {
		return null;
	}

	public MethodSignature getMethod(SolvingMethod solvingMethod) {
		return this.getSolver(solvingMethod).getMethodBySolvingMethod(solvingMethod);
	}

	public List<Solver> getSolvers() {
		return solvers;
	}

	public void addSolver(Solver solver) {
		this.solvers.add(solver);
	}

	public void removeSolver(Solver solver) {
		this.solvers.remove(solver);
	}

}
