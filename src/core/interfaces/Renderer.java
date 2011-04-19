package core.interfaces;

import core.owl.objects.Presentation;
import core.owl.objects.PresentationMethod;

public interface Renderer {

	public void run(Presentation presentation, PresentationMethod presentationMethod) throws Exception;

}
