package owlobjects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

public class OWLClassObject {
	
	private OWLClass owlClass = null;
	private OWLOntologyObjectShell objectOntology = null;
	
	private Map< String, OWLClassPropertyObject > classProperties = new TreeMap< String, OWLClassPropertyObject >();
	private Map< IRI, OWLIndividualObject > individuals = new TreeMap< IRI, OWLIndividualObject >();
	
	private int individualId = 0;
	
	public OWLClassObject( OWLOntologyObjectShell objectOntology, OWLClass owlClass ) {
		this.owlClass = owlClass;
		this.objectOntology = objectOntology;
		
		OWLOntology owlOntology = objectOntology.getOwlOntology();
		
		// Select all properties from the ontology which can be defined for this class
		Set< OWLAxiom > allAxioms = owlOntology.getAxioms();
		
		for( OWLAxiom axiom: allAxioms ) {
			if( axiom instanceof OWLPropertyDomainAxiom<?> ) {
				OWLPropertyDomainAxiom<?> domainAxiom = (OWLPropertyDomainAxiom<?>) axiom;
				
				if( ! domainAxiom.getDomain().asOWLClass().getIRI().equals( owlClass.getIRI() ) )
					continue;
				
				OWLClassPropertyObject propertyObject = new OWLClassPropertyObject( objectOntology, domainAxiom );
				classProperties.put( propertyObject.getName(), propertyObject ); 
			}
		}
		
		// Select all individuals of this class 
		for( OWLAxiom axiom: allAxioms ) {
			if( axiom instanceof OWLClassAssertionAxiom ) {
				OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) axiom;
				
				if( ! classAssertionAxiom.getClassExpression().asOWLClass().equals( owlClass.getIRI() ) )
					continue;
				
				OWLNamedIndividual namedIndividual = classAssertionAxiom.getIndividual().asOWLNamedIndividual();
				
				individuals.put( namedIndividual.getIRI(), new OWLIndividualObject( objectOntology, this, namedIndividual ) ); 
			}
		}
	}
	
	public Collection< OWLIndividualObject > getDirectIndividuals() {
		return individuals.values();
	}
	
	public Collection< OWLIndividualObject > getDescendantIndividuals() throws Exception {
		Collection< OWLIndividualObject > set = new HashSet< OWLIndividualObject >();
		set.addAll( individuals.values() );
		
		for( OWLClassObject classObject: this.getDirectSuccessors() ) {
			set.addAll( classObject.getDirectIndividuals() );
		}
		
		return set;
	}
 	
	public OWLIndividualObject spawnIndividualNumbered() {
		IRI individualIRI;
		do {
			++individualId;
			individualIRI = IRI.create( owlClass.getIRI().toString() + "." + String.valueOf( individualId ) );
		} while( this.individuals.containsKey( individualIRI ) );
		return this.spawnIndividual( String.valueOf( individualId ) ); // it must never fail
	}
	
	public OWLIndividualObject spawnIndividual( String name ) {
		IRI individualIRI = IRI.create( owlClass.getIRI().toString() + "." + name );
		
		if( this.individuals.containsKey( individualIRI ) )
			return null; // trying to spawn new individual with existing IRI - should never happen
		
		OWLIndividualObject indObj = new OWLIndividualObject( objectOntology, this, 
				this.objectOntology.getDataFactory().getOWLNamedIndividual( individualIRI ) );
		
		this.objectOntology.addAxiom( this.objectOntology.getDataFactory().getOWLClassAssertionAxiom( 
				this.owlClass, indObj.getOwlIndividual() ) );
		
		this.individuals.put( individualIRI, indObj );
		
		return indObj;
	}
	
	public Map< String, OWLClassPropertyObject > getAllProperties() {
		return this.classProperties;
	}
	
	/*public OWLClassObject getDirectParent() throws Exception {
		OWLOntology owlOntology = objectOntology.getOwlOntology();
		
		Set< OWLClassAxiom > classAxioms = owlOntology.getAxioms( owlClass );
		OWLClassObject parent = null;
		for( OWLClassAxiom axiom: classAxioms ) {
			if( axiom instanceof OWLSubClassOfAxiom ) {
				OWLSubClassOfAxiom subClassAxiom = (OWLSubClassOfAxiom) axiom;
				if( parent == null ) {
					try {
						parent = objectOntology.getClassObject( subClassAxiom.getSuperClass().asOWLClass() ); 
					}
					catch( OWLRuntimeException e ) {
						throw new Exception( "Wrong sub-class-of axiom met (superclass is not OWLClass)!" );						
					}
				}
				else
					throw new Exception( "Only one sub-class-of axiom allowed!" );
			}
		}
		return parent; // null or 1 found parent
	}*/
	
	public Collection< OWLClassObject > getDirectSuccessors() throws Exception {
		Collection< OWLClassObject > successors = new HashSet< OWLClassObject >();
		
		Set< OWLClassAxiom > classAxioms = this.objectOntology.getOwlOntology().getAxioms( owlClass );
		for( OWLClassAxiom axiom: classAxioms ) {
			if( axiom instanceof OWLSubClassOfAxiom ) {
				OWLSubClassOfAxiom subClassAxiom = (OWLSubClassOfAxiom) axiom;
				try {
					successors.add( objectOntology.getClassObject( subClassAxiom.getSubClass().asOWLClass() ) ); 
				}
				catch( OWLRuntimeException e ) {
					throw new Exception( "Wrong sub-class-of axiom met (subclass is not OWLClass)!" );
				}
			}
		}
		
		return successors;
	}
	
}
