package core.owl;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

import core.owl.base.OWLClassObject;
import core.owl.base.OWLIndividualObject;
import core.owl.objects.Task;

public class OWLOntologyObjectShell implements OWLObjectFactory {
	
	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private Map< IRI, OWLClassObject > classes = new TreeMap< IRI, OWLClassObject >();
	private Map< IRI, OWLIndividualObject > individuals = new TreeMap< IRI, OWLIndividualObject >();
	
	private OWLReasonerFactory reasonerFactory;
	private OWLReasonerConfiguration config;
	private OWLReasoner reasoner;
	
	private String ontologyAddress = null;
	
	private final String DELIMITER = "#";
	
	public IRI getClassIRIByName( String className ) {
		return IRI.create( this.getOntologyAddress() + DELIMITER + className );
	}
	
	/*public String getIndividualIRI( String className, String individualName ) {
		return this.getClassAddress( className ) + "." + individualName;
	}*/
	
	public String getOntologyAddress() {
		return this.ontologyAddress;
	}
	
	public String getEntityNameByIRI( IRI iri ) {
		String[] split = iri.toString().split( DELIMITER );
		if( split.length == 0 )
			return null;
		return split[ split.length - 1 ];
	}
	
	public OWLClassObject getClassObject( String className ) {
		return this.getClassObject( this.getClassIRIByName( className ) );
	}
	
	public OWLClassObject getClassObject( IRI classIRI ) {
		OWLClassObject result = classes.get( classIRI );
		
		if( result == null ) {
			Set< OWLClass > owlClasses = ontology.getClassesInSignature( false ); // do not include other ontologies, etc.
			
			for( OWLClass owlClass: owlClasses ) {
				IRI temp = owlClass.getIRI();
				if( temp.equals( classIRI ) ) {
					if( result != null ) {
						System.err.println( "Two or more classes with the same name met!" );
						return null; // two or more classes with the same name: how can it happen though??
					}
					result = new OWLClassObject( this, owlClass );
					classes.put( classIRI, result );
				}
			}
		}
		return result;
	}
	
	public OWLReasoner getReasoner() {
		return this.reasoner;
	}
	
	public OWLOntology getOwlOntology() {
		return ontology;
	}
	
	public OWLOntologyObjectShell( OWLOntologyManager manager, String ontologyAddress ) throws OWLOntologyCreationException {
		this.manager = manager;
		this.ontologyAddress = ontologyAddress;
		this.ontology = manager.getOntology( IRI.create( this.getOntologyAddress() ) );
		if( this.ontology == null ) {
			OWLOntologyMerger merger = new OWLOntologyMerger( manager );
			this.ontology = merger.createMergedOntology( manager, IRI.create( this.ontologyAddress ) );
			System.out.println( "Task context ontology not found! It is created by merging all known ontologies." );
		}
		
		this.reasonerFactory = new Reasoner.ReasonerFactory();
		this.config = new SimpleConfiguration( null );
		this.reasoner = this.reasonerFactory.createReasoner( this.ontology, this.config );
		this.reasoner.precomputeInferences();
	}

	public OWLClassObject getClassObject( OWLClass owlClass ) {
		try {
			return this.getClassObject( owlClass.getIRI() );
		} catch( Exception e ) {
			e.printStackTrace();
			return null; // should never happen!
		}
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

	public OWLIndividualObject getIndividualObject( IRI individualIRI ) {
		OWLIndividualObject result = individuals.get( individualIRI );
		
		if( result == null ) {
			Set< OWLNamedIndividual > individuals = ontology.getIndividualsInSignature( false ); // do not include other ontologies, etc.
			
			for( OWLNamedIndividual owlIndividual: individuals ) {
				IRI temp = owlIndividual.getIRI();
				if( temp.equals( individualIRI ) ) {
					if( result != null ) {
						System.err.println( "Two or more classes with the same name met!" );
						return null; // two or more classes with the same name: how can it happen though??
					}
					result = new OWLIndividualObject( this, owlIndividual );
					this.individuals.put( individualIRI, result );
				}
			}
		}
		return result;
	}
	
	public OWLIndividualObject createIndividual( OWLClassObject classObject ) {
		IRI individualIRI = classObject.getNewIndividualIRI();
		
		if( this.individuals.containsKey( individualIRI ) )
			return null; // trying to spawn new individual with existing IRI - should never happen
		
		OWLIndividualObject indObj = new OWLIndividualObject( this,
				this.getDataFactory().getOWLNamedIndividual( individualIRI ) );
		
		this.addAxiom( this.getDataFactory().getOWLClassAssertionAxiom( 
				classObject.getOWLClass(), indObj.getOwlIndividual() ) );
		
		this.individuals.put( individualIRI, indObj );
		
		return indObj;
	}

	public Set<Task> getTasks() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
