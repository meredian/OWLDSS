package core.owl.base;

import java.util.Map;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import core.owl.OWLOntologyObjectShell;

public class OWLIndividualObject {

	//private OWLClassObject classObject;
	//private OWLOntologyObjectShell objectOntology;
	private OWLNamedIndividual owlIndividual;
	
	//private Map< String, OWLIndividualPropertyObject > properties = new TreeMap< String, OWLIndividualPropertyObject >();
	
	public OWLIndividualObject( OWLOntologyObjectShell objectOntology, OWLNamedIndividual owlIndividual ) {
		//this.classObject = classObject;
		//this.objectOntology = objectOntology;
		this.owlIndividual = owlIndividual;
	}
	
	public OWLNamedIndividual getOwlIndividual() {
		return owlIndividual;
	}
	
	public Map< String, OWLIndividualPropertyObject > getProperties() {
		return null;
	}
	
	public OWLIndividualPropertyObject getPropertyByName( String propertyName ) {
		return null;
	}

}
