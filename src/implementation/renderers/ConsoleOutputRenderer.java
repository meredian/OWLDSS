package implementation.renderers;

import core.interfaces.Presentation;
import core.interfaces.Renderer;

public class ConsoleOutputRenderer implements Renderer {

	@Override
	public boolean Supports( Presentation presentation ) {
		return presentation.getClass() == StringPresentation.class;
	}
	
	@Override
	public void Render( Presentation presentation ) throws Exception {
		Object value = presentation.GetValue();
		if( value.getClass() == String.class ) {
			String stringValue = (String) value;
			System.out.println( stringValue );
		} else
			throw new Exception( "Presentation object is not supported" );
	}
}
