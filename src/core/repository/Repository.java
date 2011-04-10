package core.repository;

import java.util.LinkedList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLIndividual;

import core.interfaces.Adapter;

public class Repository {
	
	private List<Adapter> adapters = new LinkedList<Adapter>();
	
	public Repository() {
		// TODO Auto-generated constructor stub
	}
	
	public void addAdapter(Adapter newAdapter) {
		this.adapters.add(newAdapter);
	}
	
	public List<SolverSignature> getSolvers() {
		List<SolverSignature> solvers = new LinkedList<SolverSignature>();
		for (Adapter adapter : adapters) {
			solvers.addAll(adapter.getSolvers());
		}
		return solvers;
	}
	
	public List<SolverSignature> getSolversByTask(TaskSignature task) {
		List<SolverSignature> solvers = new LinkedList<SolverSignature>();
		for (Adapter adapter : adapters) {
			solvers.addAll(adapter.getSolversByTask(task));
		}
		return solvers;
	}
	
	public List<SolverSignature> getSolversByName(String solverName) {
		List<SolverSignature> solvers = new LinkedList<SolverSignature>();
		for (Adapter adapter : adapters) {
			solvers.addAll(adapter.getSolversByName(solverName));
		}
		return solvers;
	}

	public boolean solveTask(OWLIndividual task) {
		for (Adapter adapter : adapters) {
			if(adapter.solveTask(task)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean solveTaskBySolver(OWLIndividual task, SolverSignature solver) {
		for (Adapter adapter : adapters) {
			if(adapter.getSolvers().contains(solver)) {
				return adapter.solveTaskBySolver(task, solver);
			}
		}
		return false;
	}
}
