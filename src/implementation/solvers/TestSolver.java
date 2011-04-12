package implementation.solvers;

import java.util.ArrayList;
import java.util.HashMap;

import org.semanticweb.owlapi.model.IRI;

import core.owl.OWLIndividualBuilder;
import core.owl.OWLIndividualReader;
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
		IRI inputIRI = task.getInputObjects().iterator().next();
		OWLIndividualReader inputReader = ontologyShell.getReader(inputIRI);
		String input = inputReader.getStringValue("RowValue");
		String[] inputRow = input.split(" ");
		String output = new String();
		for (int i = 0; i < inputRow.length; ++i)
			output += inputRow[inputRow.length-1 - i] + " ";
		output = output.substring(0, output.length() - 1); // cut last space
		
		OWLIndividualBuilder resultBuilder = ontologyShell.createIndividual("Row");
		resultBuilder.addAxiom("RowValue", output);
		
		OWLIndividualBuilder presentationBuilder = ontologyShell.createIndividual("StringPresentation");
		presentationBuilder.addAxiom("Value", "Here is the row: " + output);
		
		resultBuilder.addObjectAxiom("HasPresentation", presentationBuilder.getIRI());
		task.getBuilder().addObjectAxiom("HasResult", resultBuilder.getIRI());
		
		return true;
	}

}
