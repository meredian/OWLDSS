package core.managers;

import core.owl.OWLObjectFactory;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;

public class SolverManager {

	private OWLObjectFactory objectFactory;

	public SolverManager( OWLObjectFactory objectFactory ) {
		this.objectFactory = objectFactory;
	}

	public void runSolver( SolvingMethod solvingMethod, Task task ) {
		// TODO
	}

}
