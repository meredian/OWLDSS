package core.owl.objects;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;

import core.owl.OWLIndividualReader;
import core.owl.OWLOntologyObjectShell;

public class TaskResult {

	private OWLIndividualReader individualReader;
	private OWLOntologyObjectShell ontologyShell;

	private static final String ATTRIBUTE_HAS_PRESENTATION = "HasPresentation";

	public TaskResult(OWLIndividualReader individualReader, OWLOntologyObjectShell ontologyShell) {
		this.individualReader = individualReader;
		this.ontologyShell = ontologyShell;
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
