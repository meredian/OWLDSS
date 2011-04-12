package core.owl.objects;

import java.util.Map;
import java.util.Set;

import core.owl.OWLIndividualReader;

public class ImportingMethod {

	private OWLIndividualReader individualReader;
	
	private static final String ATTRIBUTE_IMPORTER_CLASS = "ImporterClass";
	
	public ImportingMethod(OWLIndividualReader individualReader) {
		this.individualReader = individualReader;
	}

	public String getImporterClass() {
		return this.individualReader.getStringValue(ATTRIBUTE_IMPORTER_CLASS);
	}
	
	public Map<String, Set<?>> getAllAttributes() {
		return this.individualReader.getAllAttributes();
	}
	
}
