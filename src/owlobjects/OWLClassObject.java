package owlobjects;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

public class OWLClassObject {
	
	private OWLClass owlClass = null;
	private OWLOntologyObjectShell objectOntology = null;
	
	private int individualId = 0;
	
	public OWLClassObject( OWLOntologyObjectShell objectOntology, OWLClass owlClass ) {
		this.owlClass = owlClass;
		this.objectOntology = objectOntology;
	}
	
	public IRI getNewIndividualIRI() {
		return IRI.create( this.owlClass.getIRI().toString() + "." + String.valueOf( this.individualId++ ) );
	}
	
	public OWLClass getOWLClass() {
		return this.owlClass;
	}
	
	public Collection< OWLIndividualObject > getIndividuals( boolean direct ) {
		// true means direct
		NodeSet< OWLNamedIndividual > directInstances = objectOntology.getReasoner().getInstances( owlClass, direct );
		Collection< OWLIndividualObject > result = new TreeSet< OWLIndividualObject >();
		for( Node< OWLNamedIndividual > node : directInstances.getNodes() ) {
			for( OWLNamedIndividual instance : node.getEntities() ) {
				result.add( objectOntology.getIndividualObject( instance.getIRI() ) );
			}
		}
		return result; 
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
