package supervisor;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import owlobjects.OWLClassObject;
import owlobjects.OWLIndividualObject;
import owlobjects.OWLOntologyObjectShell;

public class Task {

	private Map< String, String > parametersString = new TreeMap< String, String >();
	private Map< String, Integer > parametersInteger = new TreeMap< String, Integer >();
	private Map< String, Double > parametersDouble = new TreeMap< String, Double >();
	private Map< String, Boolean > parametersBoolean = new TreeMap< String, Boolean >();
	private String className;
	
	public Task( String taskClassName ) {
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
		OWLClassObject taskClassObject = objectOntology.getClassByName( this.className );
		if( taskClassObject == null )
			return null;
		
		OWLIndividualObject individual = taskClassObject.spawnIndividualNumbered();
		
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
}
