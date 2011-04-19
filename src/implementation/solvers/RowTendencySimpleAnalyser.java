package implementation.solvers;

import java.util.Set;

import org.semanticweb.owlapi.model.IRI;

import core.owl.OWLIndividualBuilder;
import core.owl.OWLIndividualReader;
import core.owl.OWLOntologyShell;
import core.owl.objects.Task;
import core.repository.AbstractSolver;
import core.repository.MethodSignature;

public class RowTendencySimpleAnalyser extends AbstractSolver {

	private static final String METHOD_NAME = "Standard";
	private static final String ATTRIBUTE_ROW_VALUE = "RowValue";
	private static final String ATTRIBUTE_HAS_ROW = "HasRow";
	private static final String ATTRIBUTE_TENDENCY_CODE = "TendencyCode";
	private static final String CLASS_ROW_TENDENCY = "RowTendency";

	public RowTendencySimpleAnalyser() {
		this.addMethod(new MethodSignature(METHOD_NAME));
	}

	private float getAverage(float[] array, int start, int count) {
		float sum = 0;
		for (int i = 0; i < count; ++i)
			sum += array[start + i];
		return sum/count;
	}

	//1-монотонно падает, 2-колеблясь падает, 4-стабильно, 5-колеблется в пределах нормы,
	//6-не имеет тренда, 10-колеблясь растет, 11-монотонно растет
	private int getTendency(float[] array) {
		if (array.length < 5)
			return 6; // no tendency can be defined

		float newAverage = this.getAverage(array, 0, 3); // first 3 elements
		float oldAverage = this.getAverage(array, array.length - 4, 3); // last 3 elements

		float overallAverage = this.getAverage(array, 0, array.length);

		if (newAverage > overallAverage * 1.1 && oldAverage < overallAverage * 0.9)
			return 11;

		if (newAverage < overallAverage * 0.9 && oldAverage > overallAverage * 1.1)
			return 1;

		return 5;
	}

	@Override
	public boolean solveTaskByMethod(OWLOntologyShell ontologyShell, Task task, MethodSignature method) {
		if (!method.getName().equals(METHOD_NAME)) {
			System.err.println("RowTendencySimpleAnalyser: unsupported method: " + method.getName());
			return false;
		}

		Set<IRI> iris = task.getInputObjectsByClass("TimeRow");
		if (iris.size() != 1) {
			System.err.println("RowTendencySimpleAnalyser: could not parse task input objects ("
					+ iris.size() + " objects met instead of one)");
			return false;
		}

		OWLIndividualReader timeRowReader = ontologyShell.getReader(iris.iterator().next());
		String rowValue = timeRowReader.getStringValue(ATTRIBUTE_ROW_VALUE);
		rowValue = rowValue.substring(5, rowValue.length()-1); // format: "real[value1,value2,...,valueN]"

		String strings[] = rowValue.split(",");
		float values[] = new float[strings.length];

		for (int i = 0; i < strings.length; ++i)
			values[i] = Float.valueOf(strings[i]).floatValue();

		int tendency = this.getTendency(values);

		OWLIndividualBuilder builder = ontologyShell.createIndividual(CLASS_ROW_TENDENCY);

		System.out.println("RowTendencySimpleAnalyser: tendency code is " + String.valueOf(tendency));
		builder.addAxiom(ATTRIBUTE_TENDENCY_CODE, tendency);
		builder.addObjectAxiom(ATTRIBUTE_HAS_ROW, timeRowReader.getIRI());

		task.addOutputObject(builder.getIRI());
		task.setResult(builder.getIRI());

		return true;
	}

}
