package core.repository;

public class SolverStatus {

	public static final int STATUS_CODE_UNKNOWN = -1;
	public static final int STATUS_CODE_OK = 0;
	public static final int STATUS_CODE_WARNING = 1;
	public static final int STATUS_CODE_ERROR = 2;

	private final int code;
	private final String message;

	public SolverStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public SolverStatus(int code) {
		this.code = code;
		this.message = null;
	}

	public String getMessage() {
		return this.message;
	}

	public int getCode() {
		return this.code;
	}

	public static SolverStatus STATUS_UNKNOWN = new SolverStatus(STATUS_CODE_UNKNOWN);
}