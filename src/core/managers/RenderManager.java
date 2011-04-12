package core.managers;

import implementation.renderers.ConsoleOutputRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import core.interfaces.Renderer;
import core.owl.objects.Presentation;
import core.owl.objects.PresentationMethod;
import core.owl.objects.TaskResult;

public class RenderManager {

	private Renderer getRenderer(PresentationMethod presentationMethod) {
		if (presentationMethod.getRendererName().equals(ConsoleOutputRenderer.class.getSimpleName())) {
			return new ConsoleOutputRenderer();
		}
		return null;
	}

	public void process(TaskResult taskResult) throws Exception {
		if (taskResult == null) {
			System.err.println("RenderManager: nothing to render because no task result is present");
			return;
		}
		Set<Presentation> presentations = taskResult.getPresentations();
		Map<Presentation, Renderer> renderings = new HashMap<Presentation, Renderer>();

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
