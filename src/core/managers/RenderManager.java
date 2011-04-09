package core.managers;

import implementation.renderers.ConsoleOutputRenderer;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import core.interfaces.Presentation;
import core.interfaces.Renderer;
import core.supervisor.Task;

public class RenderManager {

	private Collection< Renderer > renderers = new LinkedList< Renderer >();
	
	RenderManager() {
		this.renderers.add( new ConsoleOutputRenderer() );
	}
	
	void process( Task task ) throws Exception {
		Set< Presentation > presentations = task.getPresentations();
		Map< Presentation, Set< Renderer > > renderings = new TreeMap< Presentation, Set< Renderer > >();
		
		for( Presentation presentation: presentations ) {
			Set< Renderer > availableRenderers = new HashSet< Renderer >();
			
			for( Renderer renderer: this.renderers )
				if( renderer.supports( presentation ) )
					availableRenderers.add( renderer );
			
			if( ! availableRenderers.isEmpty() )
				renderings.put( presentation, availableRenderers );
		}
		
		// TODO choose rendering!
	
		if( renderings.isEmpty() )
			throw new Exception( "No renderings available!" );
		
		Presentation chosenPresentation = renderings.keySet().iterator().next();
		Renderer chosenRenderer = renderings.get( chosenPresentation ).iterator().next();
		
		chosenRenderer.render( chosenPresentation );
	}
}
