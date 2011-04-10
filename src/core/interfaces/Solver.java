package core.interfaces;

import java.util.List;

import core.owl.objects.Task;
import core.repository.MethodSignature;

public interface Solver {

	public List<MethodSignature> getMethods();
	public List<MethodSignature> getMethodsByName(String methodName);

	public boolean solveTaskByMethod(Task task, MethodSignature solver);
	public boolean solveTask(Task task);
	/*
	 * While adapters can be cross-language, each adapter can need different options for each
	 * solver module (paths, execution options, various setting), so they can explicitly
	 * demand some setting. Cause no one knows, how all this solver stuff works except them.
	 */
	public List<String> getMandatoryParams();
	public List<String> getOptionalParams();

	public boolean addSolver(MethodSignature solver);
	public boolean removeSolver(MethodSignature solver);

	public List<MethodSignature> testMethods();
	public MethodSignature testMethod(MethodSignature solver);

}
