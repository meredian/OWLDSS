package core.managers;

import java.util.Set;

import core.interfaces.Solver;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;
import core.repository.MethodSignature;
import core.repository.SolverRepository;

public class SolverManager {

	private ImportManager importManager;
	private SolverRepository repository; 

	public SolverManager(ImportManager importManager) {
		if (importManager == null)
			throw new NullPointerException("importManager is null");
		this.importManager = importManager;
	}

	
	public void process(Task task) {
		Set<SolvingMethod> solvingMethods = task.getSolvingMethods();
		// TODO: choose solving method
		SolvingMethod solvingMethod = solvingMethods.iterator().next();
		
		importManager.process(solvingMethod, task);
		Solver solver = this.repository.getSolver(solvingMethod);
		MethodSignature method = solver.getMethodBySolvingMethod(solvingMethod);
		
		solver.solveTaskByMethod(task, method);
	}
}
