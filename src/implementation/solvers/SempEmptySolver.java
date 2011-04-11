package implementation.solvers;

import java.util.ArrayList;
import java.util.HashMap;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import core.owl.objects.Task;
import core.repository.AbstractSolver;
import core.repository.MethodSignature;
import core.repository.MethodStatus;

public class SempEmptySolver extends AbstractSolver {

	public SempEmptySolver() {
		mandatoryParams = new ArrayList<String>();
		mandatoryParams.add("MODULE_PATH");
		mandatoryParams.add("MODULE_LAUNCHER");
		options = new HashMap<String, String>();
		options.put("some_parameter", "true");
	}

	@Override
	public boolean solveTaskByMethod(Task task, MethodSignature method) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected MethodStatus callTest(MethodSignature method) {
		throw new NotImplementedException();
	}

}
