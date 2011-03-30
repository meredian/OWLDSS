package owlobjects;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

public class OWLIndividualObject {

	//private OWLClassObject classObject;
	//private OWLOntologyObjectShell objectOntology;
	private OWLNamedIndividual owlIndividual;
	
	private Map< String, OWLIndividualPropertyObject > properties = new TreeMap< String, OWLIndividualPropertyObject >();
	
	public OWLIndividualObject( OWLOntologyObjectShell objectOntology, OWLClassObject classObject, OWLNamedIndividual owlIndividual ) {
		//this.classObject = classObject;
		//this.objectOntology = objectOntology;
		this.owlIndividual = owlIndividual;
		
		Map< String, OWLClassPropertyObject > classProperties = classObject.getAllProperties();
		for( Entry< String, OWLClassPropertyObject > entry: classProperties.entrySet() ) {
			this.properties.put( entry.getValue().getName(), 
					new OWLIndividualPropertyObject( objectOntology, entry.getValue(), this ) );
		}
	}
	
	public OWLNamedIndividual getOwlIndividual() {
		return owlIndividual;
	}
	
	public Map< String, OWLIndividualPropertyObject > getProperties() {
		return properties;
	}
	
	public OWLIndividualPropertyObject getPropertyByName( String propertyName ) {
		return this.properties.get( propertyName );
	}

}
