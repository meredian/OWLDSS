package core.owl;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

import core.owl.objects.ImportingMethod;
import core.owl.objects.Presentation;
import core.owl.objects.PresentationMethod;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;
import core.owl.objects.TaskResult;

public class OWLOntologyShell implements OWLIndividualFactory {

	private OWLOntology ontology;
	private final OWLOntologyManager manager;
	private final OWLReasonerFactory reasonerFactory;
	private final OWLReasonerConfiguration config;
	private final OWLReasoner reasoner;
	private final OWLIndividualIRIFactory individualIRIFactory;

	private final String ontologyAddress;

	private final String DELIMITER = "#";

	public void dumpOntology() {
		try {
			this.manager.saveOntology( this.ontology, IRI.create( "file:" + new File("dump.owl").getAbsoluteFile() ));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("OWLOntologyShell: Unable to dump Ontology");
		}
	}

	public IRI getClassIRIByName( String className ) {
		return this.getEntityIRIByName( className );
	}

	public IRI getEntityIRIByName( String entityName ) {
		return IRI.create( this.getOntologyAddress() + DELIMITER + entityName );
	}

	public String getOntologyAddress() {
		return this.ontologyAddress;
	}

	public String getEntityNameByIRI( IRI iri ) {
		String[] split = iri.toString().split( DELIMITER );
		if( split.length == 0 )
			return null;
		return split[ split.length - 1 ];
	}

	public OWLReasoner getReasoner() {
		this.reasoner.flush();
		return this.reasoner;
	}

	public OWLOntology getOwlOntology() {
		return ontology;
	}

	public OWLOntologyShell(OWLOntologyManager manager, String ontologyAddress)
			throws OWLOntologyCreationException {
		this.manager = manager;
		this.ontologyAddress = ontologyAddress;
		this.individualIRIFactory = new OWLIndividualIRIFactory(ontologyAddress);
		this.ontology = manager.getOntology(IRI.create(this.getOntologyAddress()));
		if (this.ontology == null) {
			OWLOntologyMerger merger = new OWLOntologyMerger(manager);
			this.ontology = merger.createMergedOntology(manager, IRI.create(this.ontologyAddress));
			System.out.println("Task context ontology not found! It is created by merging all known ontologies.");
		}

		this.reasonerFactory = new Reasoner.ReasonerFactory();
		this.config = new SimpleConfiguration( null );
		this.reasoner = this.reasonerFactory.createReasoner( this.ontology, this.config );
		this.reasoner.precomputeInferences();
	}

	public void addAxiom( OWLAxiom axiom ) {
		this.manager.addAxiom( this.ontology, axiom );
	}

	public OWLDataFactory getDataFactory() {
		return manager.getOWLDataFactory();
	}

	public void removeAxiom( OWLAxiom axiom ) {
		this.manager.removeAxiom( this.ontology, axiom );
	}

	@Override
	public OWLIndividualBuilder createIndividual(String className) {
		IRI individualIRI = this.individualIRIFactory.getNewIRI(className);

		OWLNamedIndividual owlIndividual = this.getDataFactory().getOWLNamedIndividual(individualIRI);

		this.addAxiom(
			this.getDataFactory().getOWLClassAssertionAxiom(
				this.getOWLClass(IRI.create("http://www.w3.org/2002/07/owl#Thing")),
				owlIndividual
			)
		);

		this.addAxiom(
			this.getDataFactory().getOWLClassAssertionAxiom(
				this.getOWLClass(this.getClassIRIByName(className)),
				owlIndividual
			)
		);

		return new OWLIndividualBuilder(owlIndividual, this);
	}

	public Set<Task> getTasks() {
		// false = all including indirect
		Set<OWLNamedIndividual> individuals = this.getReasoner().getInstances(
				this.getOWLClass(this.getEntityIRIByName(Task.CLASS_NAME)), false
		).getFlattened();

		Set<Task> tasks = new HashSet<Task>();
		for (OWLNamedIndividual individual: individuals)
			tasks.add(this.getTask(individual.getIRI()));
		return tasks;
	}

	public Task getTask(IRI taskIRI) {
		return new Task(this.getOWLNamedIndividual(taskIRI), this.getBuilder(taskIRI),
			this.getReader(taskIRI), this);
	}

	public SolvingMethod getSolvingMethod(IRI methodIRI) {
		return new SolvingMethod(this.getReader(methodIRI), this);
	}

	public TaskResult getTaskResult(IRI taskResultIRI) {
		return new TaskResult(this.getReader(taskResultIRI), this);
	}

	public ImportingMethod getImportingMethod(IRI iri) {
		return new ImportingMethod(this.getReader(iri));
	}

	public Presentation getPresentation(IRI iri) {
		return new Presentation(this.getReader(iri), this);
	}

	public PresentationMethod getPresentationMethod(IRI iri) {
		return new PresentationMethod(this.getReader(iri));
	}

	public OWLIndividualBuilder getBuilder(IRI iri) {
		return new OWLIndividualBuilder(this.getOWLNamedIndividual(iri), this);
	}

	public OWLIndividualReader getReader(IRI iri) {
		return new OWLIndividualReader(this.getOWLNamedIndividual(iri), this);
	}

	public void addDataPropertyAssertionAxiom(IRI individualIRI, String propertyName, OWLLiteral value) {
		this.manager.addAxiom(
			this.ontology,
			this.getDataFactory().getOWLDataPropertyAssertionAxiom(
				this.getOWLDataProperty(this.getEntityIRIByName(propertyName)),
				this.getOWLNamedIndividual(individualIRI),
				value
			)
		);
	}

	public OWLNamedIndividual getOWLNamedIndividual(IRI individualIRI) {
		Set<OWLNamedIndividual> individuals = this.ontology.getIndividualsInSignature(true); // include imports closure
		for (OWLNamedIndividual individual: individuals)
			if (individual.getIRI().equals(individualIRI))
				return individual;
		return this.getDataFactory().getOWLNamedIndividual(individualIRI);
	}

	public OWLDataProperty getOWLDataProperty(IRI dataPropertyIRI) {
		Set<OWLDataProperty> dataProperties = this.ontology.getDataPropertiesInSignature(true); // include imports closure
		for (OWLDataProperty dataProperty: dataProperties)
			if (dataProperty.getIRI().equals(dataPropertyIRI))
				return dataProperty;
		return this.getDataFactory().getOWLDataProperty(dataPropertyIRI);
	}

	public OWLObjectProperty getOWLObjectProperty(IRI objectPropertyIRI) {
		Set<OWLObjectProperty> objectProperties = this.ontology.getObjectPropertiesInSignature(true); // include imports closure
		for (OWLObjectProperty objectProperty: objectProperties)
			if (objectProperty.getIRI().equals(objectPropertyIRI))
				return objectProperty;
		return this.getDataFactory().getOWLObjectProperty(objectPropertyIRI);
	}

	public OWLClass getOWLClass(IRI classIRI) {
		Set<OWLClass> classes = this.ontology.getClassesInSignature(true); // include imports closure
		for (OWLClass clazz: classes)
			if (clazz.getIRI().equals(classIRI))
				return clazz;
		return this.getDataFactory().getOWLClass(classIRI);
	}

	public void addObjectPropertyAssertionAxiom(IRI individualIRI, String propertyName, IRI objectIRI) {
		this.manager.addAxiom(
				this.ontology,
				this.getDataFactory().getOWLObjectPropertyAssertionAxiom(
					this.getOWLObjectProperty(this.getEntityIRIByName(propertyName)),
					this.getOWLNamedIndividual(individualIRI),
					this.getOWLNamedIndividual(objectIRI)
				)
			);
	}

}
