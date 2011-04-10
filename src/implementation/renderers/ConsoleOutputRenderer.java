package implementation.renderers;

import core.interfaces.Renderer;
import core.owl.base.OWLIndividualPropertyObject;
import core.owl.objects.Presentation;

public class ConsoleOutputRenderer implements Renderer {

	@Override
	public void run( Presentation presentation ) throws Exception {
		OWLIndividualPropertyObject property = presentation.get();
		if( property.getStringValue() != null )
			System.out.println( property.getStringValue() );
		else
			throw new Exception( "Presentation object is not supported" );
	}
}
