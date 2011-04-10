package core.repository;

public class MethodStatus {

	public static final int STATUS_CODE_UNKNOWN = -1;
	public static final int STATUS_CODE_OK = 0;
	public static final int STATUS_CODE_WARNING = 1;
	public static final int STATUS_CODE_ERROR = 2;

	private final int code;
	private final String message;

	public MethodStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public MethodStatus(int code) {
		this.code = code;
		this.message = null;
	}

	public String getMessage() {
		return this.message;
	}

	public int getCode() {
		return this.code;
	}

	public static MethodStatus STATUS_UNKNOWN = new MethodStatus(STATUS_CODE_UNKNOWN);
}