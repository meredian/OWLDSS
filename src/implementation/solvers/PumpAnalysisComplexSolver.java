package implementation.solvers;

import org.semanticweb.owlapi.model.IRI;

import core.owl.OWLOntologyShell;
import core.owl.objects.Task;
import core.repository.AbstractSolver;
import core.repository.MethodSignature;

public class PumpAnalysisComplexSolver extends AbstractSolver {

	private static final String METHOD_NAME = "Standard";
	private static final String FIRST_TASK_CLASS = "RowTendencyAnalysisTask";
	private static final String SECOND_TASK_CLASS = "PumpTendencyAnalysisTask";

	private static final String FIRST_INPUT = "PumpEfficiencyDeviationRow";

	public PumpAnalysisComplexSolver() {
		this.addMethod(new MethodSignature(METHOD_NAME));
	}

	@Override
	public boolean solveTaskByMethod(OWLOntologyShell ontologyShell, Task task, MethodSignature method) {

		if (method.getName().equals(METHOD_NAME)) {
			splitTask(ontologyShell, task);
			return true;
		}
		return false;
	}

	private void splitTask(OWLOntologyShell ontologyShell, Task task) {
		IRI rowAnalysisTaskIRI = ontologyShell.createIndividual(FIRST_TASK_CLASS).getIRI();
		IRI pumpAnalysisTaskIRI = ontologyShell.createIndividual(SECOND_TASK_CLASS).getIRI();

		Task rowAnalysisTask = new Task(ontologyShell.getOWLNamedIndividual(rowAnalysisTaskIRI),
				ontologyShell.getBuilder(rowAnalysisTaskIRI), ontologyShell.getReader(rowAnalysisTaskIRI),
				ontologyShell);

		rowAnalysisTask.addInput(task.getImportedObjectsByClass(FIRST_INPUT));

		Task pumpAnalysisTask = new Task(ontologyShell.getOWLNamedIndividual(pumpAnalysisTaskIRI),
				ontologyShell.getBuilder(pumpAnalysisTaskIRI), ontologyShell.getReader(pumpAnalysisTaskIRI),
				ontologyShell);

		pumpAnalysisTask.addInput(task.getInputObjects());
		pumpAnalysisTask.addImport(task.getImportedObjects());
		pumpAnalysisTask.addSubTask(rowAnalysisTask);

		task.addOutputObject(rowAnalysisTaskIRI);
		task.addOutputObject(pumpAnalysisTaskIRI);
	}

}
