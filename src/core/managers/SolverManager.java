package core.managers;

import java.util.Set;

import core.interfaces.Solver;
import core.owl.OWLOntologyObjectShell;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;
import core.repository.MethodSignature;
import core.repository.SolverRepository;

public class SolverManager {

	private ImportManager importManager;
	private SolverRepository repository;
	private OWLOntologyObjectShell ontologyShell;

	public SolverManager(OWLOntologyObjectShell ontologyShell, ImportManager importManager) {
		if (importManager == null)
			throw new NullPointerException("importManager is null");
		this.repository = new SolverRepository();
		this.importManager = importManager;
		this.ontologyShell = ontologyShell;
	}


	public void process(Task task) {
		Set<SolvingMethod> solvingMethods = task.getSolvingMethods();
		// TODO: choose solving method
		SolvingMethod solvingMethod = solvingMethods.iterator().next();

		importManager.process(solvingMethod, task);
		Solver solver = this.repository.getSolver(solvingMethod);
		MethodSignature method = solver.getMethodBySolvingMethod(solvingMethod);

		solver.solveTaskByMethod(this.ontologyShell, task, method);
	}
}
