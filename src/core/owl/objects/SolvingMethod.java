package core.owl.objects;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.model.IRI;

import core.owl.OWLIndividualReader;
import core.owl.OWLOntologyShell;

public class SolvingMethod {

	OWLIndividualReader individualReader;
	OWLOntologyShell ontologyShell;

	private static final String ATTRIBUTE_METHOD_NAME = "MethodName";
	private static final String ATTRIBUTE_SOLVER_CLASS = "SolverClass";
	private static final String ATTRIBUTE_USES_IMPORTER = "UsesImporter";

	public SolvingMethod(OWLIndividualReader individualReader, OWLOntologyShell ontologyShell) {
		this.individualReader = individualReader;
		this.ontologyShell = ontologyShell;
	}

	public String getMethodName() {
		return this.individualReader.getStringValue(ATTRIBUTE_METHOD_NAME);
	}

	public String getSolverClassName() {
		return this.individualReader.getStringValue(ATTRIBUTE_SOLVER_CLASS);
	}

	public Set<ImportingMethod> getImportingMethods() {
		Set<IRI> taskIRIs = this.individualReader.getObjectValues(ATTRIBUTE_USES_IMPORTER);
		Set<ImportingMethod> result = new TreeSet<ImportingMethod>();
		for (IRI iri: taskIRIs)
			result.add(ontologyShell.getImportingMethod(iri));
		return result;
	}

	public Map<String, Set<?>> getAllAttributes() {
		return this.individualReader.getAllAttributes();
	}
}
