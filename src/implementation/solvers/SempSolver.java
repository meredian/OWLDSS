package implementation.solvers;

import java.io.BufferedReader;
import java.io.IOException;
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
		//createData(method);
		//compileData(method);
		compileModule(method);
		executeModule(method);
		return true;
	}

	private void createData(MethodSignature method) {
		System.out.println("CREATING DATA");
		// TODO Auto-generated method stub

	}

	private void compileData(MethodSignature method) {
		System.out.println("COMPILING DATA");
		String compileCommand = "cd " + method.getParam("MODULE_PATH") + " && wine pmc "
				+ method.getParam("MODULE_DATA_INPUT") + "-s -e -w";
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
		executeCommandAndReadOutput(executeCommand);
		return null;
	}

	private void executeCommandAndReadOutput(String command) {
		try {
			ProcessBuilder procBuilder = new ProcessBuilder("/bin/sh", "-c", command);

			Process process = procBuilder.start();

			InputStream stdout = process.getInputStream();
			InputStreamReader isrStdout = new InputStreamReader(stdout);
			BufferedReader brStdout = new BufferedReader(isrStdout);

			String line = null;
			while ((line = brStdout.readLine()) != null) {
				System.out.println(line);
			}
			process.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
