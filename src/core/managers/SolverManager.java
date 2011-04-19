package core.managers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import core.interfaces.Solver;
import core.owl.OWLOntologyShell;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;
import core.repository.MethodSignature;
import core.repository.SolverRepository;
import core.utils.MethodSelectionMode;

public class SolverManager {

	private final ImportManager importManager;
	private final SolverRepository repository;
	private final OWLOntologyShell ontologyShell;
	private final MethodSelectionMode methodSelectionMode;

	public SolverManager(OWLOntologyShell ontologyShell, ImportManager importManager,
			MethodSelectionMode methodSelectionMode) {
		if (importManager == null)
			throw new NullPointerException("importManager is null");
		this.repository = new SolverRepository();
		this.importManager = importManager;
		this.ontologyShell = ontologyShell;
		this.methodSelectionMode = methodSelectionMode;
	}

	private SolvingMethod selectMethod(Set<SolvingMethod> solvingMethods) {
		SolvingMethod selectedMethod = null;
		// 1. Select by importers count TODO
		// 2. Select by avoiding subtasks TODO

		// 3. Choose manually if available
		if (this.methodSelectionMode.isAllowManualSelection() && selectedMethod == null) {
			System.out.println("SolverManager: Manual selection mode is available. Please select a number from list:");
			Map<Integer, SolvingMethod> methodMap = new HashMap<Integer, SolvingMethod>();
			int i = 0;
			for (SolvingMethod solvingMethod: solvingMethods) {
				methodMap.put(Integer.valueOf(++i), solvingMethod);
				System.out.println(String.valueOf(i) + ") " + solvingMethod.getMethodName() + " of solver " + solvingMethod.getSolverClassName());
			}
			Integer selectedNum = null;
			while (selectedNum == null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String input;
				try {
					input = reader.readLine();
					selectedNum = Integer.valueOf(input);
				} catch (Exception e) {
					System.out.println("SolverManager: Could not parse your answer. Please enter just a number:");
					selectedNum = null;
				}
			}

			selectedMethod = methodMap.get(selectedNum);
		}

		// 4. If still no method
		if (selectedMethod == null)
			return solvingMethods.iterator().next();
		else
			return selectedMethod;
	}

	public void process(Task task) {
		System.out.println("SolverManager: processing task " + task.getReader().getIRI().toString());
		Set<SolvingMethod> solvingMethods = task.getSolvingMethods();
		System.out.println("SolverManager: " + String.valueOf(solvingMethods.size()) + " solving methods found");
		if (solvingMethods.isEmpty()) {
			System.err.println("SolverManager: task procession aborted");
			return;
		}

		SolvingMethod solvingMethod;
		if (solvingMethods.size() > 1)
			solvingMethod = this.selectMethod(solvingMethods);
		else
			solvingMethod = solvingMethods.iterator().next();

		System.out.println("SolverManager: method '" + solvingMethod.getMethodName() + "' of solver '" +
				solvingMethod.getSolverClassName() + "' will be ran");

		importManager.process(solvingMethod, task);
		Solver solver = this.repository.getSolver(solvingMethod);
		MethodSignature method = solver.getMethodBySolvingMethod(solvingMethod);

		solver.solveTaskByMethod(this.ontologyShell, task, method);
		System.out.println("SolverManager: method finished");
	}
}
