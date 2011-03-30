package owlobjects;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

public class OWLIndividualPropertyObject {
	
	private OWLOntologyObjectShell objectOntology;
	private OWLClassPropertyObject classPropertyObject;
	private OWLIndividualObject individualObject;
	private OWLLiteral dataValue = null;
	private OWLNamedIndividual objectValue = null; // value object of this individual property
	
	public String getStringValue() {
		if( dataValue == null || !dataValue.getDatatype().isString() )
			return null;
		return dataValue.getLiteral();
	}
	
	public void setStringValue( String value ) throws Exception {
		this.persistDataValue( objectOntology.getDataFactory().getOWLLiteral( value ) );
	}
	
	public Integer getIntegerValue() {
		if( dataValue == null || !dataValue.getDatatype().isInteger() )
			return null;
		return dataValue.parseInteger();
	}
	
	public void setIntegerValue( int value ) throws Exception {
		this.persistDataValue( objectOntology.getDataFactory().getOWLLiteral( value ) );
	}
	
	public Double getDoubleValue() {
		if( dataValue == null || !dataValue.getDatatype().isDouble() )
			return null;
		return dataValue.parseDouble();
	}
	
	public void setDoubleValue( double value ) throws Exception {
		this.persistDataValue( objectOntology.getDataFactory().getOWLLiteral( value ) );
	}
	
	public Boolean getBooleanValue() {
		if( dataValue == null || !dataValue.getDatatype().isDouble() )
			return null;
		return dataValue.parseBoolean();
	}
	
	public void purgeDataValue() {
		if( dataValue == null )
			return;
		
		Set< OWLDataPropertyAssertionAxiom > axioms = 
			objectOntology.getOwlOntology().getDataPropertyAssertionAxioms( individualObject.getOwlIndividual() );
		
		for( OWLDataPropertyAssertionAxiom axiom: axioms ) {
			if( axiom.getProperty().asOWLDataProperty().getIRI().equals( classPropertyObject.getOwlProperty().getIRI() ) )
				objectOntology.removeAxiom( axiom );
		}
	}
	
	public void setBooleanValue( boolean value ) throws Exception {
		this.persistDataValue( objectOntology.getDataFactory().getOWLLiteral( value ) );
	}
	
	private void persistDataValue( OWLLiteral value ) throws Exception {
		if( !classPropertyObject.isDataProperty() )
			throw new Exception( "Trying to set data value for an object individual property" );
		if( dataValue != null )
			throw new Exception( "Trying to set data value that has already been set" );
		
		this.dataValue = value;

		OWLDataProperty owlDataProperty = (OWLDataProperty) classPropertyObject.getOwlProperty();
		objectOntology.addAxiom( objectOntology.getDataFactory().getOWLDataPropertyAssertionAxiom( 
				owlDataProperty, individualObject.getOwlIndividual(), this.dataValue ) );
	}
	
	public OWLNamedIndividual getObjectValue() {
		return objectValue;
	}
	
	public void setObjectValue( OWLNamedIndividual object ) throws Exception {
		if( classPropertyObject.isDataProperty() )
			throw new Exception( "Trying to set object value for a data individual property" );
		if( objectValue != null )
			throw new Exception( "Trying to set object value that has already been set" );

		// property range match check will be done automatically
		
		this.objectValue = object;
		OWLObjectProperty owlObjectProperty = (OWLObjectProperty) classPropertyObject.getOwlProperty();
		objectOntology.addAxiom( objectOntology.getDataFactory().getOWLObjectPropertyAssertionAxiom( 
				owlObjectProperty, individualObject.getOwlIndividual(), object ) );
	}
	
	public OWLIndividualPropertyObject( OWLOntologyObjectShell objectOntology, OWLClassPropertyObject classPropertyObject, 
			OWLIndividualObject individualObject ) {
		this.objectOntology = objectOntology;
		this.classPropertyObject = classPropertyObject;
		this.individualObject = individualObject;
		
		OWLOntology owlOntology = objectOntology.getOwlOntology();
		
		if( classPropertyObject.isDataProperty() ) {
			Set< OWLDataPropertyAssertionAxiom > axioms = owlOntology.getDataPropertyAssertionAxioms( individualObject.getOwlIndividual() );
			for( OWLDataPropertyAssertionAxiom axiom: axioms ) {
				if( axiom.getProperty().asOWLDataProperty().getIRI().equals( 
						classPropertyObject.getOwlProperty().getIRI() ) ) {
					//dataPropertyAxiom = axiom;
					dataValue = axiom.getObject();
				}
			}
		} else {
			Set< OWLObjectPropertyAssertionAxiom > axioms = owlOntology.getObjectPropertyAssertionAxioms( individualObject.getOwlIndividual() );
			for( OWLObjectPropertyAssertionAxiom axiom: axioms ) {
				if( axiom.getProperty().asOWLObjectProperty().getIRI().equals( 
						classPropertyObject.getOwlProperty().getIRI() ) ) {
					//objectPropertyAxiom = axiom;
					objectValue = axiom.getObject().asOWLNamedIndividual();
				}
			}
		}
	}
	
}
