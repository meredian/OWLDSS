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
		System.out.println("RenderManager: processing task result");
		if (taskResult == null) {
			System.err.println("RenderManager: nothing to render because no task result is present");
			return;
		}
		Set<Presentation> presentations = taskResult.getPresentations();
		Map<Presentation, Renderer> renderings = new HashMap<Presentation, Renderer>();

		System.out.println("RenderManager: listing all available renderings...");
		int counter = 0;
		for (Presentation presentation : presentations)
			for (PresentationMethod method : presentation.getPresentationMethods()) {
				System.out.println("RenderManager: " + String.valueOf(++counter) + ") presentation " 
						+ presentation.getType() + ", renderer " + method.getRendererName());
				renderings.put(presentation, this.getRenderer(method));
			}

		System.out.println("RenderManager: random rendering will be chosen");
		// TODO choose rendering!

		if (renderings.isEmpty())
			throw new Exception("No renderings available!");

		Presentation chosenPresentation = renderings.keySet().iterator().next();
		Renderer chosenRenderer = renderings.get(chosenPresentation);

		chosenRenderer.run(chosenPresentation);
		System.out.println("RenderManager: rendering finished");
	}
}
