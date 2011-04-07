package renderers;

public interface Renderer {
	
	abstract public boolean supports( Presentation presentation );
	
	abstract public void run( Presentation presentation ) throws Exception;
	
}
