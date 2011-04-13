package core.owl.objects;

import java.util.Map;
import java.util.Set;

import core.owl.OWLIndividualReader;

public class PresentationMethod {

	private OWLIndividualReader individualReader;

	private static final String ATTRIBUTE_RENDERER_CLASS = "RendererClass";

	public PresentationMethod(OWLIndividualReader individualReader) {
		this.individualReader = individualReader;
	}

	public String getRendererName() {
		return this.individualReader.getStringValue(ATTRIBUTE_RENDERER_CLASS);
	}

	public Map<String, Set<?>> getAllAttributes() {
		return this.individualReader.getAllAttributes();
	}

}
