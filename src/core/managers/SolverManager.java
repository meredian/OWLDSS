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
		System.out.println("SolverManager: processing task " + task.getReader().getIRI().toString());
		Set<SolvingMethod> solvingMethods = task.getSolvingMethods();
		System.out.println("SolverManager: " + String.valueOf(solvingMethods.size()) + " solving methods found; " +
				"random one will be chosen");
		// TODO: choose solving method
		SolvingMethod solvingMethod = solvingMethods.iterator().next();
		System.out.println("SolverManager: method '" + solvingMethod.getMethodName() + "' of solver '" + 
				solvingMethod.getSolverClassName() + "' will be ran");

		importManager.process(solvingMethod, task);
		Solver solver = this.repository.getSolver(solvingMethod);
		MethodSignature method = solver.getMethodBySolvingMethod(solvingMethod);

		solver.solveTaskByMethod(this.ontologyShell, task, method);
		System.out.println("SolverManager: method finished");
	}
}
