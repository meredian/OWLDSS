package core.managers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import core.interfaces.Renderer;
import core.owl.objects.Presentation;
import core.owl.objects.PresentationMethod;
import core.owl.objects.TaskResult;
import core.utils.MethodSelectionMode;

public class RenderManager {

	MethodSelectionMode methodSelectionMode;

	private class Rendering {
		private final Presentation presentation;
		private final PresentationMethod presentationMethod;

		public Rendering(Presentation presentation, PresentationMethod presentationMethod) {
			this.presentation = presentation;
			this.presentationMethod = presentationMethod;
		}

		public Presentation getPresentation() {
			return this.presentation;
		}

		public PresentationMethod getPresentationMethod() {
			return this.presentationMethod;
		}
	};

	public RenderManager(MethodSelectionMode methodSelectionMode) {
		this.methodSelectionMode = methodSelectionMode;
	}

	private Renderer getRenderer(PresentationMethod presentationMethod) {
		try {
			Renderer renderer = (Renderer) Class.forName(presentationMethod.getRendererName()).newInstance();
			return renderer;
		} catch (Exception e) {
			System.err.println("RenderManager: Could not instantiate renderer " + presentationMethod.getRendererName() +
					". Rendering will be aborted.");
		}
		return null;
	}

	private Rendering selectRendering(Set<Rendering> renderings) {
		Rendering selectedRendering = null;
		// 1. Select by preferred presentation TODO
		// 2. Select by preferred renderer TODO

		// 3. Choose manually if available
		if (this.methodSelectionMode.isAllowManualSelection() && selectedRendering == null) {
			System.out.println("SolverManager: Manual selection mode is available. Please select a number from list:");
			Map<Integer, Rendering> methodMap = new HashMap<Integer, Rendering>();
			int i = 0;
			for (Rendering rendering: renderings) {
				methodMap.put(Integer.valueOf(++i), rendering);
				System.out.println(String.valueOf(i) + ") " + rendering.getPresentation().getType() +
						" using renderer " + rendering.getPresentationMethod().getRendererName());
			}
			Integer selectedNum = null;
			while (selectedNum == null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String input;
				try {
					input = reader.readLine();
					selectedNum = Integer.valueOf(input);
				} catch (Exception e) {
					System.out.println("SolverManager: Could not parse your answer. Please enter just a number:");
					selectedNum = null;
				}
			}

			selectedRendering = methodMap.get(selectedNum);
		}

		// 4. If still no method
		if (selectedRendering == null)
			return renderings.iterator().next();
		else
			return selectedRendering;
	}

	public void process(TaskResult taskResult) throws Exception {
		System.out.println("RenderManager: processing task result");
		if (taskResult == null) {
			System.err.println("RenderManager: nothing to render because no task result is present");
			return;
		}
		Set<Presentation> presentations = taskResult.getPresentations();
		Set<Rendering> renderings = new HashSet<Rendering>();

		for (Presentation presentation : presentations)
			for (PresentationMethod method : presentation.getPresentationMethods())
				renderings.add(new Rendering(presentation, method));

		if (renderings.isEmpty())
			throw new Exception("No renderings available!");

		Rendering selectedRendering;
		if (renderings.size() > 1)
			selectedRendering = this.selectRendering(renderings);
		else
			selectedRendering = renderings.iterator().next();

		Presentation selectedPresentation = selectedRendering.getPresentation();
		Renderer selectedRenderer = this.getRenderer(selectedRendering.getPresentationMethod());

		if (selectedRenderer == null)
			return;
		System.out.println("RenderManager: Processing presentation of type " + selectedPresentation.getType() +
				" using renderer " + selectedRendering.getPresentationMethod().getRendererName());

		selectedRenderer.run(selectedPresentation, selectedRendering.getPresentationMethod());
		System.out.println("RenderManager: rendering finished");
	}
}
