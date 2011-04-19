package core.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import core.owl.OWLOntologyObjectShell;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;
import core.repository.MethodSignature;

public interface Solver {

	public Collection<MethodSignature> getMethods();
	public MethodSignature getMethodByName(String name);
	public MethodSignature getMethodBySolvingMethod(SolvingMethod solvingMethod);

	public boolean solveTaskByMethod(OWLOntologyObjectShell ontologyShell, Task task, MethodSignature method);

	/*
	 * While adapters can be cross-language, each adapter can need different options for each
	 * solver module (paths, execution options, various setting), so they can explicitly
	 * demand some setting. Cause no one knows, how all this solver stuff works except them.
	 */
	public List<String> getMandatoryParams();
	public Map<String,String> getOptions();

	public boolean addMethod(MethodSignature method);
	public boolean removeMethod(MethodSignature method);

	public Collection<MethodSignature> testMethods();
	public MethodSignature testMethod(MethodSignature method);

}
