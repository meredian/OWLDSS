package owlobjects;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

public class OWLOntologyObjectShell {
	
	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private Map< String, OWLClassObject > classes = new TreeMap< String, OWLClassObject >();
	
	private OWLReasonerFactory reasonerFactory;
	private OWLReasonerConfiguration config;
	private OWLReasoner reasoner;
	
	private String ontologyAddress = null;
	
	private final String DELIMITER = "#";
	
	public String getClassAddress( String className ) {
		return this.getOntologyAddress() + DELIMITER + className;
	}
	
	public String getIndividualAddress( String className, String individualName ) {
		return this.getClassAddress( className ) + "." + individualName;
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
	
	public OWLClassObject getClassByName( String className ) {
		OWLClassObject result = classes.get( className );
		IRI classIRI = IRI.create( this.getClassAddress( className ) );
		
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
					classes.put( className, result );
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
			return this.getClassByName( this.getEntityNameByIRI( owlClass.getIRI() ) );
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
	
}
