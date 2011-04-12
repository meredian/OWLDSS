package core.owl;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;


public class OWLIndividualReader {

	private OWLNamedIndividual owlIndividual;
	private OWLOntologyObjectShell ontologyShell;
	
	public OWLIndividualReader(OWLNamedIndividual owlIndividual, OWLOntologyObjectShell ontologyShell) {
		this.owlIndividual = owlIndividual;
		this.ontologyShell = ontologyShell;
	}
	
	public String tryGetClassName() {
		Set<OWLClass> classes = ontologyShell.getReasoner().getTypes(this.owlIndividual, true).getFlattened(); // true = direct types!
		
		classes.removeAll(ontologyShell.getReasoner().getTopClassNode().getEntities());
		
		if (classes.size() > 1) {
			System.err.println("An individual belongs to more than one direct class excluding top ones");
			return null;
		}
		
		if (classes.isEmpty()) {
			System.err.println("An individual has no direct classes excluding top ones");
			return null;
		}
		
		return this.ontologyShell.getEntityNameByIRI( classes.iterator().next().getIRI() );
	}
	
	public IRI getIRI() {
		return this.owlIndividual.getIRI();
	}
	
	public OWLLiteral getSingleDataValue( String attribute ) {
		Set<OWLLiteral> values = this.ontologyShell.getReasoner().getDataPropertyValues(owlIndividual, 
			this.ontologyShell.getOWLDataProperty( 
				this.ontologyShell.getEntityIRIByName(attribute)
			)
		);
		
		if (values.isEmpty()) {
			System.err.println("Cannot get data value because no value can be inferred");
			return null;
		}
			
		
		if (values.size() > 1) {
			System.err.println("Cannot get data value due to relation ambiguity");
			return null;
		}
		
		return values.iterator().next();
	}
	
	/**
	 * @return non-null Set object, even if attribute name is invalid, etc.
	 */
	public Set<IRI> getObjectValuesByClass(String attribute, String owlClassName) {
		Set<OWLNamedIndividual> values = this.ontologyShell.getReasoner().getObjectPropertyValues(
			owlIndividual, 
			this.ontologyShell.getOWLObjectProperty( 
				this.ontologyShell.getEntityIRIByName(attribute)
			)
		).getFlattened();
		
		Set<IRI> result = new TreeSet<IRI>();
		
		OWLClass owlClass = owlClassName == null ? null : 
			this.ontologyShell.getOWLClass(
				this.ontologyShell.getEntityIRIByName(owlClassName)
			);
		
		for (OWLNamedIndividual individual: values)
			if (owlClass == null || 
				ontologyShell.getReasoner().getTypes(individual, true).containsEntity(owlClass)
			)
				result.add(individual.getIRI());
		
		return result;
	}
	
	/**
	 * @return non-null Set object, even if attribute name is invalid, etc.
	 */
	public Set<IRI> getObjectValues(String attribute) {
		return this.getObjectValuesByClass(attribute, null);
	}
	
	public String getStringValue(String attribute) {
		OWLLiteral literal = this.getSingleDataValue(attribute);
		if (literal == null)
			return null;
		else
			return literal.getLiteral();
	}
	
	public Integer getIntegerValue(String attribute) {
		OWLLiteral literal = this.getSingleDataValue(attribute);
		if (literal == null)
			return null;
		else
			return literal.parseInteger();
	}
	
	public Double getDoubleValue(String attribute) {
		OWLLiteral literal = this.getSingleDataValue(attribute);
		if (literal == null)
			return null;
		else
			return literal.parseDouble();
	}
	
	public Boolean getBooleanValue(String attribute) {
		OWLLiteral literal = this.getSingleDataValue(attribute);
		if (literal == null)
			return null;
		else
			return literal.parseBoolean();
	}
	
	public IRI getSingleObjectValue(String attribute) {
		Set<IRI> values = this.getObjectValues(attribute);
		if (values == null || values.isEmpty() || values.size() > 1) {
			System.err.println("Cannot get single object value");
			return null;
		}
		
		return values.iterator().next();
	}
	
	/**
	 * @return set of OWLLiteral objects if a property is a data one,
	 * 		   set of object IRI-s if a property is an object one
	 */
	public Map<String, Set<?>> getAllAttributes() {
		Map<String, Set<?>> result = new TreeMap<String, Set<?>>();
		
		// true = include imports closure
		Set<OWLDataProperty> allDataProperties = 
			this.ontologyShell.getOwlOntology().getDataPropertiesInSignature(true);
		
		for (OWLDataProperty property: allDataProperties) {
			Set<OWLLiteral> literals = this.ontologyShell.getReasoner().getDataPropertyValues(owlIndividual, property);
			if (!literals.isEmpty())
				result.put(ontologyShell.getEntityNameByIRI(property.getIRI()), literals);
		}
		
		Set<OWLObjectProperty> allObjectProperties = 
			this.ontologyShell.getOwlOntology().getObjectPropertiesInSignature(true);
		
		for (OWLObjectProperty property: allObjectProperties) {
			Set<OWLNamedIndividual> individuals =
				this.ontologyShell.getReasoner().getObjectPropertyValues(owlIndividual, property).getFlattened();
			Set<IRI> iris = new TreeSet<IRI>();
			for (OWLNamedIndividual individual: individuals)
				iris.add(individual.getIRI());
			
			if (!iris.isEmpty())
				result.put(ontologyShell.getEntityNameByIRI(property.getIRI()), iris);
		}
		
		return result;
	}
	
}
