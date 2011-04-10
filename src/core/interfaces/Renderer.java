package core.interfaces;

import core.owl.objects.Presentation;


public interface Renderer {

	public boolean supports(Presentation presentation);

	public void render(Presentation presentation) throws Exception;

}
