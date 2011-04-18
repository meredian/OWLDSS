package implementation.solvers;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;

import core.owl.OWLOntologyObjectShell;
import core.owl.objects.Task;
import core.repository.AbstractSolver;
import core.repository.MethodSignature;

public class PumpAnalysisComplexSolver extends AbstractSolver {
	
	private static final String METHOD_NAME = "Standard";
	private static final String FIRST_TASK_CLASS = "RowTendencyAnalysisTask";
	private static final String SECOND_TASK_CLASS = "PumpTendencyAnalysisTask";
	
	private static final String FIRST_INPUT = "PumpEfficiencyDeviationRow";
	
	public PumpAnalysisComplexSolver() {
		init();
		MethodSignature efficiencyTrendAnalysis = new MethodSignature(METHOD_NAME);
		this.addMethod(efficiencyTrendAnalysis);
	}

	@Override
	public boolean solveTaskByMethod(OWLOntologyObjectShell ontologyShell, Task task, MethodSignature method) {
		
		if ( method.getName().equals(METHOD_NAME)) {
			splitTask( ontologyShell, task );
			return true;
		}
		return false;
	}

	private Set<Task> splitTask(OWLOntologyObjectShell ontologyShell, Task task) {
		IRI rowAnalysisTaskIRI = ontologyShell.createIndividual(FIRST_TASK_CLASS).getIRI();
		IRI pumpAnalysisTaskIRI = ontologyShell.createIndividual(SECOND_TASK_CLASS).getIRI();
		
		Task rowAnalysisTask =  new Task(ontologyShell.getOWLNamedIndividual(rowAnalysisTaskIRI), 
				ontologyShell.getBuilder(rowAnalysisTaskIRI),
				ontologyShell.getReader(rowAnalysisTaskIRI), ontologyShell);
		
		rowAnalysisTask.addInput(task.getImportedObjectsByClass(FIRST_INPUT));
		
		Task pumpAnalysisTask =  new Task(ontologyShell.getOWLNamedIndividual(pumpAnalysisTaskIRI), 
				ontologyShell.getBuilder(pumpAnalysisTaskIRI),
				ontologyShell.getReader(pumpAnalysisTaskIRI), ontologyShell);

		pumpAnalysisTask.addInput(task.getInputObjects());
		pumpAnalysisTask.addImport(task.getImportedObjects());
		pumpAnalysisTask.addSubTask(rowAnalysisTask);
		
		Set<Task> result = new HashSet<Task>(2);
		result.add(rowAnalysisTask);
		result.add(pumpAnalysisTask);
		return result;
	}

}