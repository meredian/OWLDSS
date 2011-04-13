package implementation.solvers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import core.owl.OWLOntologyObjectShell;
import core.owl.objects.Task;
import core.repository.AbstractSolver;
import core.repository.MethodSignature;

public class SempSolver extends AbstractSolver {

	public SempSolver() {
		init();
		mandatoryParams.add("MODULE_PATH");
		mandatoryParams.add("MODULE_LAUNCHER");
		mandatoryParams.add("MODULE_DATA_INPUT");
	}

	@Override
	public boolean solveTaskByMethod(OWLOntologyObjectShell ontologyShell, Task task, MethodSignature method) {
		createData(method);
		compileData(method);
		compileModule(method);
		executeModule(method);
		return true;
	}

	private void createData(MethodSignature method) {
		System.out.println("CREATING DATA");

		String data = "uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;\n" +
		"uses Global_Ontology;\n"+
		"rule Создание_данных\n"+
		"=>\n" +
		"new\n" +
		"	nsngdu: ReferenceData_OAGA(fd : 20.0, errkpdmax: 20.0),\n" +
		"	ns_nu1: ReferenceData_PumpingUnit(name:\"Марка1\"),\n" +
		"	pu1:  PumpingUnit(name: \"pu1\",  errkpd: real[0.19,0.19,0.18,0.18,0.20,0.19,0.20,0.19,0.20,0.19,0.19,0.20], ns: ns_nu1);\n" +
		"end;";

		String createCommand = "cd " + method.getParam("MODULE_PATH") + " && echo '" + data + "' | iconv -f UTF8 -t CP1251 > "
		+ method.getParam("MODULE_DATA_INPUT");
		executeCommandAndReadOutput(createCommand);
	}

	private void compileData(MethodSignature method) {
		System.out.println("COMPILING DATA");
		String compileCommand = "cd " + method.getParam("MODULE_PATH") + " && wine pmc "
				+ method.getParam("MODULE_DATA_INPUT") + " -s -e -w";
		executeCommandAndReadOutput(compileCommand);
	}

	private void compileModule(MethodSignature method) {
		System.out.println("COMPILING MODULE");
		String compileCommand = "cd " + method.getParam("MODULE_PATH") + " && wine cmd /c \\!.bat";
		executeCommandAndReadOutput(compileCommand);
	}

	private String executeModule(MethodSignature method) {
		System.out.println("EXECUTING MODULE");
		String executeCommand = "cd " + method.getParam("MODULE_PATH") + " && wine pmc "
				+ method.getParam("MODULE_LAUNCHER") + "| iconv -f CP1251 -t UTF8";
		String result = executeCommandAndReadOutput(executeCommand);
		try {
			result = result.substring(result.lastIndexOf("<individuals>"), result.lastIndexOf("</individuals>"));
			return result;
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}

	private String executeCommandAndReadOutput(String command) {
		try {
			System.out.println("Command :: " + command);
			ProcessBuilder procBuilder = new ProcessBuilder("/bin/sh", "-c", command);

			Process process = procBuilder.start();

			InputStream stdout = process.getInputStream();
			InputStreamReader isrStdout = new InputStreamReader(stdout);
			BufferedReader brStdout = new BufferedReader(isrStdout);

			String line = null;
			String result = null;
			while ((line = brStdout.readLine()) != null) {
				result += line;
				System.out.println(line);
			}
			process.waitFor();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
