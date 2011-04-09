package implementation.solvers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SempUniversalSolver {

	public String getOutput() {
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(
					"/bin/sh",
					"-c",
					"wine pmc C:\\\\semp\\\\modules\\\\EfficiencyTrendAnalysis\\\\EfficiencyTrendAnalysis_Launcher.pm -m "
							+ "C:\\\\semp\\\\modules\\\\EfficiencyTrendAnalysis | iconv -f CP1251 -t UTF8");

			procBuilder.redirectErrorStream(true);

			Process process = procBuilder.start();

			InputStream stdout = process.getInputStream();
			InputStreamReader isrStdout = new InputStreamReader(stdout);
			BufferedReader brStdout = new BufferedReader(isrStdout);

			String line = null;
			while ((line = brStdout.readLine()) != null) {
				System.out.println(line);
			}

			int exitVal = process.waitFor();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
