package core.utils;

public class MethodSelectionMode {
	
	private boolean allowManualSelection = true;
	private boolean avoidSubtasks = false;
	private boolean tryToUseLessImporters = false;
	private String preferredPresentation = null;
	private String preferredRenderingMethod = null;
	
	public MethodSelectionMode() {
	}
	
	public MethodSelectionMode(boolean allowManualSelection, boolean avoidSubtasks, 
			boolean tryToUseLessImporters) {
		this.allowManualSelection = allowManualSelection;
		this.avoidSubtasks = avoidSubtasks;
		this.tryToUseLessImporters = tryToUseLessImporters;
	}
	
	public void setPreferredRenderingMethod(String preferredRenderingMethod) {
		this.preferredRenderingMethod = preferredRenderingMethod;
	}

	public String getPreferredRenderingMethod() {
		return preferredRenderingMethod;
	}

	public void setPreferredPresentation(String preferredPresentation) {
		this.preferredPresentation = preferredPresentation;
	}

	public String getPreferredPresentation() {
		return preferredPresentation;
	}

	public boolean isAllowManualSelection() {
		return allowManualSelection;
	}

	public boolean isAvoidSubtasks() {
		return avoidSubtasks;
	}

	public boolean isTryToUseLessImporters() {
		return tryToUseLessImporters;
	}	
	
}
