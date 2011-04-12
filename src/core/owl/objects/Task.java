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
		IRI taskIRI = this.individualReader.getSingleObjectValue(ATTRIBUTE_HAS_SUPERTASK);
		if( taskIRI == null )
			return null;
		else
			return ontologyShell.getTask(taskIRI);
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
		return this.ontologyShell.getTaskResult(individualReader.getSingleObjectValue(ATTRIBUTE_HAS_RESULT));
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
		return this.individualReader.getBooleanValue(ATTRIBUTE_IS_SOLVED) == null ? Status.QUEUED : Status.SOLVED;
	}
	
	public OWLIndividualReader getReader() {
		return this.individualReader;
	}
	
	public OWLIndividualBuilder getBuilder() {
		return this.individualBuilder;
	}
}
