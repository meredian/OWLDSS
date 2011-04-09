package implementation.renderers;

import core.interfaces.Presentation;
import core.owl.objects.OWLIndividualObject;

public class StringPresentation implements Presentation {

	private String value;

	public StringPresentation( OWLIndividualObject individualObject ) {
		this.value = individualObject.getProperties().get( Presentation.VALUE_FIELD_NAME ).getStringValue();
	}

	@Override
	public String getValue() {
		return this.value;
	}

}
