package core.owl.objects;

import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import core.owl.OWLIndividualBuilder;
import core.owl.OWLIndividualReader;
import core.owl.OWLOntologyObjectShell;

public class Task {
	
	public enum Status {
		QUEUED, SOLVED;
	}
	
	OWLNamedIndividual owlIndividual;
	OWLIndividualBuilder individualBuilder;
	OWLIndividualReader individualReader;
	OWLOntologyObjectShell ontologyShell;
	
	public static final String CLASS_NAME = "Task";
	private static final String ATTRIBUTE_HAS_SUBTASK = "HasSubTask";
	private static final String ATTRIBUTE_HAS_SUPERTASK = "HasSuperTask";
	private static final String ATTRIBUTE_SOLVED_BY = "SolvedBy";
	private static final String ATTRIBUTE_HAS_INPUT = "HasInput";
	private static final String ATTRIBUTE_HAS_OUTPUT = "HasOutput";
	private static final String ATTRIBUTE_HAS_IMPORT = "HasImport";
	private static final String ATTRIBUTE_HAS_RESULT = "HasResult";
	private static final String ATTRIBUTE_IS_SOLVED = "IsSolved";
	
	public Task(OWLNamedIndividual owlIndividual, OWLIndividualBuilder individualBuilder, 
			OWLIndividualReader individualReader, OWLOntologyObjectShell ontologyShell) {
		this.owlIndividual = owlIndividual;
		this.individualBuilder = individualBuilder;
		this.individualReader = individualReader;
		this.ontologyShell = ontologyShell;
	}
	
	public Task getSuperTask() {
		Set<IRI> taskIRIs = this.individualReader.getObjectValues(ATTRIBUTE_HAS_SUPERTASK);
		if( taskIRIs.isEmpty() )
			return null;
		else
			// let somebody else take care of uniqueness of the super-task
			return ontologyShell.getTask(taskIRIs.iterator().next()); 
	}
	
	public Set<Task> getSubTasks() {
		Set<IRI> taskIRIs = this.individualReader.getObjectValues(ATTRIBUTE_HAS_SUBTASK);
		Set<Task> result = new TreeSet<Task>();
		for (IRI iri: taskIRIs)
			result.add(ontologyShell.getTask(iri));
		return result;
	}

	public Set<SolvingMethod> getSolvingMethods() {
		Set<IRI> taskIRIs = this.individualReader.getObjectValues(ATTRIBUTE_SOLVED_BY);
		Set<SolvingMethod> result = new TreeSet<SolvingMethod>();
		for (IRI iri: taskIRIs)
			result.add(ontologyShell.getSolvingMethod(iri));
		return result;
	}
	
	public Set<IRI> getInputObjects() {
		return this.individualReader.getObjectValues(ATTRIBUTE_HAS_INPUT);
	}
	
	public Set<IRI> getImportedObjects() {
		return this.individualReader.getObjectValues(ATTRIBUTE_HAS_IMPORT);
	}
	
	public TaskResult getResult() {
		Set<IRI> resultIRIs = individualReader.getObjectValues(ATTRIBUTE_HAS_RESULT);
		if (resultIRIs.isEmpty())
			return null;
		
		if (resultIRIs.size() > 1)
			System.err.println("Task has " + String.valueOf(resultIRIs.size()) + " results! Random one will be chosen.");
		
		return this.ontologyShell.getTaskResult(resultIRIs.iterator().next());
	}
	
	public Set<Task> getOutputTasks() {
		Set<IRI> taskIRIs = this.individualReader.getObjectValuesByClass(ATTRIBUTE_HAS_OUTPUT, CLASS_NAME);
		Set<Task> result = new TreeSet<Task>();
		for (IRI iri: taskIRIs)
			result.add(ontologyShell.getTask(iri));
		return result;
	}

	public void addSubTask(Task outputTask) {
		this.individualBuilder.addObjectAxiom(ATTRIBUTE_HAS_SUBTASK, outputTask.getIRI());		
	}
	
	private IRI getIRI() {
		return this.owlIndividual.getIRI();
	}

	public void markAsSolved() {
		this.individualBuilder.addAxiom(ATTRIBUTE_IS_SOLVED, true);
	}
	
	public Status getStatus() {
		return this.individualReader.checkDataValueExists(ATTRIBUTE_IS_SOLVED) ? Status.SOLVED : Status.QUEUED;
	}
	
	public OWLIndividualReader getReader() {
		return this.individualReader;
	}
	
	public OWLIndividualBuilder getBuilder() {
		return this.individualBuilder;
	}
}
