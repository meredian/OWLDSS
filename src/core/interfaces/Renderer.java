package core.interfaces;


public interface Renderer {

	public boolean supports(Presentation presentation);

	public void render(Presentation presentation) throws Exception;

}
