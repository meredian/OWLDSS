package core.owl;

import java.util.Map;
import java.util.TreeMap;

import org.semanticweb.owlapi.model.IRI;

public class OWLIndividualIRIFactory {
	
	private Map<String, Integer> newClassIdentifiers = new TreeMap<String, Integer>();
	private String ontologyAddress;
	
	public OWLIndividualIRIFactory(String ontologyAddress) {
		this.ontologyAddress = ontologyAddress;
	}
	
	public IRI getNewIRI(String className) {
		if (!newClassIdentifiers.containsKey(className))
			newClassIdentifiers.put(className, Integer.valueOf(0));
		int newNumber = newClassIdentifiers.get(className).intValue();
		newClassIdentifiers.put(className, new Integer(newNumber+1));
		return IRI.create(ontologyAddress + "#" + className + "_" + String.valueOf(newNumber));
	}
	
}
