package implementation.solvers;

import java.util.ArrayList;
import java.util.HashMap;

import core.owl.OWLOntologyObjectShell;
import core.owl.objects.Task;
import core.repository.AbstractSolver;
import core.repository.MethodSignature;
import core.repository.MethodStatus;

public class TestSolver extends AbstractSolver {

	public TestSolver() {
		this.mandatoryParams = new ArrayList<String>();
		this.methods = new ArrayList<MethodSignature>();
		this.options = new HashMap<String, String>();
		this.addMethod(new MethodSignature("RowInvertMethod"));
	}

	@Override
	protected MethodStatus callTest(MethodSignature method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean solveTaskByMethod(OWLOntologyObjectShell ontologyShell, Task task, MethodSignature method) {
		// TODO Auto-generated method stub
		return false;
	}

}
