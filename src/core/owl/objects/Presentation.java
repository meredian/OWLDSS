package core.owl.objects;

import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import core.owl.OWLIndividualReader;
import core.owl.OWLOntologyObjectShell;

public class Presentation {

	private OWLIndividualReader individualReader;
	//private OWLIndividualBuilder individualBuilder;
	private OWLOntologyObjectShell ontologyShell;

	private static final String ATTRIBUTE_SUPPORTED_BY_PRESENTATION_METHOD = "SupportedByPresentationMethod";
	private static final String ATTRIBUTE_VALUE = "PresentationStringValue"; // TODO

	public Presentation(/*OWLIndividualBuilder individualBuilder, */OWLIndividualReader individualReader,
			OWLOntologyObjectShell ontologyShell) {
		//this.individualBuilder = individualBuilder;
		this.individualReader = individualReader;
		this.ontologyShell = ontologyShell;
	}

	public String getType() {
		return this.individualReader.tryGetClassName();
	}

	public OWLLiteral getLiteralValue() {
		return this.individualReader.getSingleDataValue(ATTRIBUTE_VALUE);
	}

	public Set<IRI> getObjectValue() {
		return this.individualReader.getObjectValues(ATTRIBUTE_VALUE);
	}

	public Set<PresentationMethod> getPresentationMethods() {
		Set<IRI> iris = this.individualReader.getObjectValues(ATTRIBUTE_SUPPORTED_BY_PRESENTATION_METHOD);
		Set<PresentationMethod> result = new TreeSet<PresentationMethod>();
		for (IRI iri: iris)
			result.add(ontologyShell.getPresentationMethod(iri));
		return result;
	}
}
