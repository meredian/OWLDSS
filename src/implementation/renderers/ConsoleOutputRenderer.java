package implementation.renderers;

import core.interfaces.Renderer;
import core.owl.objects.Presentation;
import core.owl.objects.PresentationMethod;

public class ConsoleOutputRenderer implements Renderer {

	@Override
	public void run(Presentation presentation, PresentationMethod presentationMethod) throws Exception {
		String value = presentation.getLiteralValue().getLiteral();
		if( value != null )
			System.out.println( "ConsoleOutputRenderer: " + value );
		else
			throw new Exception( "Presentation object is not supported" );
	}
}
