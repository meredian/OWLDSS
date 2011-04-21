package core.utils;

public class Platform {
	private static final boolean isWindows = System.getProperty("os.name").contains("Win");
	public static final int WINDOWS = 0;
	public static final int UNIX = 1;
	
	public static int getOs() {
		if ( isWindows )
			return WINDOWS;
		else
			return UNIX;
	}
	
}
