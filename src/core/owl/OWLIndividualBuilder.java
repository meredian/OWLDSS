package core.owl;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;


public class OWLIndividualBuilder {

	private OWLNamedIndividual owlIndividual;
	private OWLOntologyObjectShell ontologyShell;
	
	public OWLIndividualBuilder(OWLNamedIndividual owlIndividual, OWLOntologyObjectShell ontologyShell) {
		this.owlIndividual = owlIndividual;
		this.ontologyShell = ontologyShell;
	}
	
	public void addAxiom( String attributeName, String value ) {
		this.persistDataValue( attributeName, this.ontologyShell.getDataFactory().getOWLLiteral( value ) );
	}
	
	public void addAxiom( String attributeName, int value ) {
		this.persistDataValue( attributeName, this.ontologyShell.getDataFactory().getOWLLiteral( value ) );
	}
	
	public void addAxiom( String attributeName, double value ) {
		this.persistDataValue( attributeName, this.ontologyShell.getDataFactory().getOWLLiteral( value ) );
	}
	
	public void addAxiom( String attributeName, boolean value ) {
		this.persistDataValue( attributeName, this.ontologyShell.getDataFactory().getOWLLiteral( value ) );
	}
	
	private void persistDataValue( String attributeName, OWLLiteral value ) {
		OWLDataProperty owlDataProperty =
			this.ontologyShell.getDataFactory().getOWLDataProperty(
				this.ontologyShell.getEntityIRIByName(attributeName)
			);
		
		this.ontologyShell.addAxiom( this.ontologyShell.getDataFactory().getOWLDataPropertyAssertionAxiom( 
				owlDataProperty, this.owlIndividual, value ) );
	}
	
	public void addObjectAxiom( String attributeName, IRI objectIRI ) {
		OWLDataFactory dataFactory = this.ontologyShell.getDataFactory();
		
		OWLObjectProperty owlObjectProperty =
			dataFactory.getOWLObjectProperty(this.ontologyShell.getEntityIRIByName(attributeName));
		
		this.ontologyShell.addAxiom( 
			dataFactory.getOWLObjectPropertyAssertionAxiom( 
				owlObjectProperty, this.owlIndividual, dataFactory.getOWLNamedIndividual(objectIRI) 
			)
		);
	}

	public IRI getIRI() {
		return this.owlIndividual.getIRI();
	}
}
