package implementation.solvers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLLiteral;

import core.owl.OWLIndividualReader;
import core.owl.OWLOntologyObjectShell;
import core.owl.objects.Task;
import core.repository.AbstractSolver;
import core.repository.MethodSignature;
import core.utils.IndividualXMLParser;

public class SempSolver extends AbstractSolver {

	public SempSolver() {
		init();
		mandatoryParams.add("MODULE_PATH");
		mandatoryParams.add("MODULE_LAUNCHER");
		mandatoryParams.add("MODULE_DATA_INPUT");
		mandatoryParams.add("MODULE_CREATE_DATA_HEADER");
		mandatoryParams.add("MODULE_CREATE_DATA_TAIL");
		options.put("some_param", "true");
	}

	@Override
	public boolean solveTaskByMethod(OWLOntologyObjectShell ontologyShell, Task task, MethodSignature method) {
		createData(ontologyShell, task, method);
		//compileData(method);
		//compileModule(method);
		String result = compileAndExecuteModule(method);
		IndividualXMLParser parser = new IndividualXMLParser(ontologyShell, result);
		for (IRI iri: parser.getAllIndividuals())
			task.addOutputObject(iri);
		if (parser.getResultIndividual() != null)
			task.setResult(parser.getResultIndividual());
		System.out.println("SempSolver: solver finished");
		return parser.getResultIndividual() != null;
	}

	private void createData(OWLOntologyObjectShell ontologyShell, Task task, MethodSignature method) {
		System.out.println("SempSolver: creating data file");
		Set<IRI> objects = task.getInputObjects();
		objects.addAll(task.getImportedObjects());
		
		String content = method.getParam("MODULE_CREATE_DATA_HEADER");
		
		boolean firstTime = true;
		
		Map<IRI, Integer> enumerator = new HashMap<IRI, Integer>();
		int counter = 0;
		for (IRI iri: objects) {
			OWLIndividualReader reader = ontologyShell.getReader(iri);
			String className = reader.tryGetClassName();
			
			if (!firstTime)
				content += ",\r\n";
			firstTime = false;
			
			enumerator.put(iri, ++counter);
			content += "object" + String.valueOf(counter) + ": " + className + "( iri: \"" + iri.toString() + "\"";
			
			Map<String, Set<?>> attributes = reader.getAllAttributes();
			for (Entry<String, Set<?>> attribute: attributes.entrySet()) {
				if (attribute.getKey().equals("topObjectProperty")) // a small hack to get rid of some warnings
					continue;
				Set<?> valueSet = attribute.getValue();
				if (valueSet.isEmpty())
					continue;
				if (valueSet.size() > 1) {
					System.err.println("SempSolver: I don't know how to interpret multi-value attributes. " +
							"Attribute " + attribute.getKey() + " will be dumped.");
					continue;
				}
				
				Object value = valueSet.iterator().next();
				
				String strValue = null;
				
				try {
					strValue = ((OWLLiteral) value).getLiteral();
				} catch (Exception e) {
					IRI objectIRI = (IRI) value;
					Integer objId = enumerator.get(objectIRI);
					if (objId != null)
						strValue = "object" + String.valueOf(objId);
					else
						continue; // outside relations are ignored
				}

				content += ", " + attribute.getKey() + ": " + strValue;
			}
			
			content += " )";
		}
		
		content += ";\r\n" + method.getParam("MODULE_CREATE_DATA_TAIL");
		
		File createDataFile = new File(method.getParam("MODULE_DATA_INPUT"));
		FileOutputStream stream;
		try {
			stream = new FileOutputStream(createDataFile);
			stream.write(content.getBytes());
			stream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String compileAndExecuteModule(MethodSignature method) {
		System.out.println("SempSolver: executing module");
		String executeCommand = "cd " + method.getParam("MODULE_PATH") + " && wine pmc "
				+ method.getParam("MODULE_LAUNCHER") + " -w | iconv -f CP1251 -t UTF8";
		String result = executeCommandAndReadOutput(executeCommand);
		try {
			String opening = new String("<individuals>");
			String closing = new String("</individuals>");
			result = result.substring(result.lastIndexOf(opening), result.lastIndexOf(closing) + closing.length());
			return result;
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}

	private String executeCommandAndReadOutput(String command) {
		try {
			System.out.println("SempSolver: executing command: " + command);
			ProcessBuilder procBuilder = new ProcessBuilder("/bin/sh", "-c", command);

			Process process = procBuilder.start();

			InputStream stdout = process.getInputStream();
			InputStreamReader isrStdout = new InputStreamReader(stdout);
			BufferedReader brStdout = new BufferedReader(isrStdout);

			String line = null;
			String result = null;
			while ((line = brStdout.readLine()) != null) {
				result += line;
				System.out.println(" | " + line);
			}
			process.waitFor();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
