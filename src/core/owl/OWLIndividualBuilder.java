package core.owl;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;


public class OWLIndividualBuilder {

	private OWLNamedIndividual owlIndividual;
	private OWLOntologyObjectShell ontologyShell;
	
	public OWLIndividualBuilder(OWLNamedIndividual owlIndividual, OWLOntologyObjectShell ontologyShell) {
		this.owlIndividual = owlIndividual;
		this.ontologyShell = ontologyShell;
	}
	
	public void addAxiom( String attributeName, String value ) {
		this.ontologyShell.addDataPropertyAssertionAxiom(this.getIRI(), attributeName, 
				this.ontologyShell.getDataFactory().getOWLLiteral( value ));
	}
	
	public void addAxiom( String attributeName, int value ) {
		this.ontologyShell.addDataPropertyAssertionAxiom(this.getIRI(), attributeName, 
				this.ontologyShell.getDataFactory().getOWLLiteral( value ));
	}
	
	public void addAxiom( String attributeName, double value ) {
		this.ontologyShell.addDataPropertyAssertionAxiom(this.getIRI(), attributeName, 
				this.ontologyShell.getDataFactory().getOWLLiteral( value ));
	}
	
	public void addAxiom( String attributeName, boolean value ) {
		this.ontologyShell.addDataPropertyAssertionAxiom(this.getIRI(), attributeName, 
				this.ontologyShell.getDataFactory().getOWLLiteral( value ));
	}
	
	public void addObjectAxiom( String attributeName, IRI objectIRI ) {
		this.ontologyShell.addObjectPropertyAssertionAxiom(owlIndividual.getIRI(), attributeName, objectIRI);
	}

	public IRI getIRI() {
		return this.owlIndividual.getIRI();
	}
}
