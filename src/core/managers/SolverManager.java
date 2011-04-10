package core.managers;

import java.util.Set;

import core.interfaces.Solver;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;

public class SolverManager {

	private ImportManager importManager;

	public SolverManager(ImportManager importManager) {
		this.importManager = importManager;
	}
	
	private Solver getSolver(SolvingMethod method) {
		// TODO
		return null;
	}

	public void process(Task task) {
		Set<SolvingMethod> solvingMethods = task.getSolvingMethods();
		
		// TODO: choose solving method
		SolvingMethod solvingMethod = solvingMethods.iterator().next();
		
		Solver solver = this.getSolver(solvingMethod);
		
		importManager.process(solvingMethod, task);
		solver.run(task);
	}

}
