package core.supervisor;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import core.interfaces.Presentation;
import core.owl.OWLOntologyObjectShell;
import core.owl.objects.OWLClassObject;
import core.owl.objects.OWLIndividualObject;

public class Task {

	private Map< String, String > parametersString = new TreeMap< String, String >();
	private Map< String, Integer > parametersInteger = new TreeMap< String, Integer >();
	private Map< String, Double > parametersDouble = new TreeMap< String, Double >();
	private Map< String, Boolean > parametersBoolean = new TreeMap< String, Boolean >();
	private String className;
	
	public Task( String taskClassName ) {
		if ( taskClassName == null)
			throw new NullPointerException("taskClassName is null");
		this.className = taskClassName;
	}
	
	public void addParameter( String name, String value ) {
		parametersString.put( name, value );
	}
	
	public void addParameter( String name, Integer value ) {
		parametersInteger.put( name, value );
	}
	
	public void addParameter( String name, Double value ) {
		parametersDouble.put( name, value );
	}
	
	public void addParameter( String name, Boolean value ) {
		parametersBoolean.put( name, value );
	}

	public OWLIndividualObject putInto( OWLOntologyObjectShell objectOntology ) {
		OWLClassObject taskClassObject = objectOntology.getClassObject( this.className );
		if( taskClassObject == null )
			return null;
		
		OWLIndividualObject individual = objectOntology.createIndividual( objectOntology.getClassObject( this.className ) );
		
		try {
			for( Entry< String, String > entry: parametersString.entrySet() )
				individual.getPropertyByName( entry.getKey() ).setStringValue( entry.getValue() );
			for( Entry< String, Integer > entry: parametersInteger.entrySet() )
				individual.getPropertyByName( entry.getKey() ).setIntegerValue( entry.getValue() );
			for( Entry< String, Double > entry: parametersDouble.entrySet() )
				individual.getPropertyByName( entry.getKey() ).setDoubleValue( entry.getValue() );
			for( Entry< String, Boolean > entry: parametersBoolean.entrySet() )
				individual.getPropertyByName( entry.getKey() ).setBooleanValue( entry.getValue() );
			
			return individual;
		} catch( Exception e ) {
			System.err.println( "Cannot not translate task into OWL representation!" );
			e.printStackTrace();
			return null;
		}
	}

	public Set< Presentation > getPresentations() {
		// TODO Auto-generated method stub
		return null;
	}
}