package core.interfaces;

import java.util.List;

import org.semanticweb.owlapi.model.OWLIndividual;

import core.repository.SolverSignature;
import core.repository.TaskSignature;

public interface Adapter {

	public List<SolverSignature> getSolvers();
	public List<SolverSignature> getSolversByTask(TaskSignature task);
	public List<SolverSignature> getSolversByName(String solverName);

	public boolean solveTask(OWLIndividual task);
	public boolean solveTaskBySolver(OWLIndividual task, SolverSignature solver);

	/*
	 * While adapters can be cross-language, each adapter can need different options for each
	 * solver module (paths, execution options, various setting), so they can explicitly
	 * demand some setting. Cause no one knows, how all this solver stuff works except them.
	 */
	public List<String> getMandatoryParams();
	public List<String> getOptionalParams();

	public boolean addSolver(SolverSignature solver);
	public boolean removeSolver(SolverSignature solver);

	public List<SolverSignature> testSolvers();
	public SolverSignature testSolver(SolverSignature solver);

}
