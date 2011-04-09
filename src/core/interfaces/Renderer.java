package core.interfaces;


public interface Renderer {

	public boolean Supports(Presentation presentation);

	public void Render(Presentation presentation) throws Exception;

}
