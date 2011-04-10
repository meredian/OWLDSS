package core.repository;

import java.util.LinkedList;
import java.util.List;

import core.interfaces.Solver;
import core.owl.objects.Task;

public class Repository {
	
	private List<Solver> solvers = new LinkedList<Solver>();
	
	public Repository() {
		// TODO Auto-generated constructor stub
	}
	
	public void addAdapter(Solver newAdapter) {
		this.solvers.add(newAdapter);
	}
	
	public List<MethodSignature> getSolvers() {
		List<MethodSignature> methods = new LinkedList<MethodSignature>();
		for (Solver solver : solvers) {
			methods.addAll(solver.getMethods());
		}
		return methods;
	}
	
	public List<MethodSignature> getSolversByName(String solverName) {
		List<MethodSignature> methods = new LinkedList<MethodSignature>();
		for (Solver solver : solvers) {
			methods.addAll(solver.getMethodsByName(solverName));
		}
		return methods;
	}

	public boolean solveTaskBySolver(Task task, MethodSignature method) {
		for (Solver solver : solvers) {
			if(solver.getMethods().contains(solver)) {
				return solver.solveTaskByMethod(task, method);
			}
		}
		return false;
	}
}
