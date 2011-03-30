package owlobjects;

import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLPropertyDomainAxiom;

public class OWLClassPropertyObject {

	private OWLPropertyDomainAxiom<?> owlDomainAxiom;
	private boolean dataProperty;
	private String name;
	
	public OWLClassPropertyObject( OWLOntologyObjectShell objectOntology, OWLPropertyDomainAxiom<?> owlDomainAxiom ) {
		this.owlDomainAxiom = owlDomainAxiom;
		
		OWLProperty<?, ?> property = (OWLProperty<?, ?>) this.owlDomainAxiom.getProperty();
		
		this.dataProperty = property.isDataPropertyExpression();
		this.name = objectOntology.getEntityNameByIRI( property.getIRI() );
	}
	
	public OWLNamedObject getOwlProperty() {
		return (OWLProperty<?, ?>) owlDomainAxiom.getProperty();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isDataProperty() {
		return dataProperty;
	}
	
}
