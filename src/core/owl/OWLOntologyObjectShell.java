package core.owl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import core.owl.objects.ImportingMethod;
import core.owl.objects.Presentation;
import core.owl.objects.PresentationMethod;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;
import core.owl.objects.TaskResult;

public class OWLOntologyObjectShell implements OWLIndividualFactory {
	
	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private OWLReasonerFactory reasonerFactory;
	private OWLReasonerConfiguration config;
	private OWLReasoner reasoner;
	private OWLIndividualIRIFactory individualIRIFactory;
	
	private String ontologyAddress;
	
	private final String DELIMITER = "#";
	
	public void dumpOntology() {
		try {
			this.manager.saveOntology( this.ontology, IRI.create( "file:/home/where-is-s/src/OWLDSS/dump.owl" ) );
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
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
		return this.reasoner;
	}
	
	public OWLOntology getOwlOntology() {
		return ontology;
	}

	public OWLOntologyObjectShell(OWLOntologyManager manager, String ontologyAddress) 
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
	
	public OWLIndividualBuilder createIndividual(String className) {
		IRI individualIRI = this.individualIRIFactory.getNewIRI(className);
		
		OWLNamedIndividual owlIndividual = this.getDataFactory().getOWLNamedIndividual(individualIRI);
		
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
		Set<OWLNamedIndividual> individuals = this.reasoner.getInstances(
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
	
	private OWLIndividualBuilder getBuilder(IRI iri) {
		return new OWLIndividualBuilder(this.getOWLNamedIndividual(iri), this);
	}
	
	private OWLIndividualReader getReader(IRI iri) {
		return new OWLIndividualReader(this.getOWLNamedIndividual(iri), this);
	}

	private OWLIndividualBuilder createIndividualFromXML(Element individualElement) throws Exception {
		NodeList childNodes = individualElement.getChildNodes();
		
		String individualClass = individualElement.getAttribute("class");
		OWLIndividualBuilder builder = this.createIndividual(individualClass);
		
		for (int i = 0; i < childNodes.getLength(); ++i) {
			// get the attribute element
			Element attrElement = (Element) childNodes.item(i);
			assert(attrElement.getTagName().equals("attr"));
			
			String attrType = attrElement.getAttribute("type");
			String attrName = attrElement.getAttribute("name");
			
			if (attrType.equals("object")) {
				NodeList attrObjects = attrElement.getChildNodes();
				for (int j = 0; j < attrObjects.getLength(); ++j) {
					// get the attribute element
					Element subIndividual = (Element) attrObjects.item(j);
					IRI subIndividualIRI = this.createIndividualFromXML(subIndividual).getIRI();
					builder.addObjectAxiom(attrName, subIndividualIRI);
				}
			} else { 
				String attrValue = attrElement.getAttribute("value");
				if (attrType.equals("string"))
					builder.addAxiom(attrName, attrValue);
				else if (attrType.equals("int"))
					builder.addAxiom(attrName, Integer.parseInt(attrValue));
				else if (attrType.equals("double"))
					builder.addAxiom(attrName, Double.parseDouble(attrValue));
				else if (attrType.equals("boolean"))
					builder.addAxiom(attrName, Boolean.parseBoolean(attrValue));
				else
					throw new Exception("Wrong attribute type met (" + attrType + ")!");
			}
			
		}
		
		return builder;
	}
	
	public void createIndividualsFromXML(String taskXML) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(taskXML.getBytes());
			Document xml = db.parse(stream);
			Element rootElement = xml.getDocumentElement();
			this.createIndividualFromXML(rootElement);
		} catch(Exception e) {
			e.printStackTrace();
		}
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
