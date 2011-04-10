package core.managers;

import implementation.renderers.ConsoleOutputRenderer;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import core.interfaces.Renderer;
import core.owl.objects.Presentation;
import core.owl.objects.PresentationMethod;
import core.owl.objects.TaskResult;

public class RenderManager {

	private Renderer getRenderer(PresentationMethod presentationMethod) {
		if (presentationMethod.getRendererName() == ConsoleOutputRenderer.class.getSimpleName()) {
			return new ConsoleOutputRenderer(); 
		}
		return null;
	}

	public void process(TaskResult taskResult) throws Exception {
		Set<Presentation> presentations = taskResult.getPresentations();
		Map<Presentation, Renderer> renderings = new TreeMap<Presentation, Renderer>();

		for (Presentation presentation : presentations)
			for (PresentationMethod method : presentation.getPresentationMethods())
				renderings.put(presentation, this.getRenderer(method));

		// TODO choose rendering!

		if (renderings.isEmpty())
			throw new Exception("No renderings available!");

		Presentation chosenPresentation = renderings.keySet().iterator().next();
		Renderer chosenRenderer = renderings.get(chosenPresentation);

		chosenRenderer.run(chosenPresentation);
	}
}
