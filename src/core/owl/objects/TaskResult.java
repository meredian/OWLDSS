package core.owl.objects;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;

import core.owl.OWLIndividualReader;
import core.owl.OWLOntologyShell;

public class TaskResult {

	private final OWLIndividualReader individualReader;
	private final OWLOntologyShell ontologyShell;

	private static final String ATTRIBUTE_HAS_PRESENTATION = "HasPresentation";

	public TaskResult(OWLIndividualReader individualReader, OWLOntologyShell ontologyShell) {
		this.individualReader = individualReader;
		this.ontologyShell = ontologyShell;
	}

	public IRI getIRI() {
		return this.individualReader.getIRI();
	}

	public Map<String, Set<?>> getAllAttributes() {
		return this.individualReader.getAllAttributes();
	}

	public Set<Presentation> getPresentations() {
		Set<IRI> iris = this.individualReader.getObjectValues(ATTRIBUTE_HAS_PRESENTATION);
		Set<Presentation> result = new TreeSet<Presentation>();
		for (IRI iri: iris)
			result.add(this.ontologyShell.getPresentation(iri));
		return result;
	}

}
