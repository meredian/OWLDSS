package core.managers;

import java.util.Set;

import core.interfaces.Importer;
import core.owl.OWLOntologyShell;
import core.owl.objects.ImportingMethod;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;

public class ImportManager {

	private final OWLOntologyShell ontologyShell;

	public ImportManager(OWLOntologyShell ontologyShell) {
		if (ontologyShell == null)
			throw new NullPointerException("ontologyShell is null");
		this.ontologyShell = ontologyShell;
	}

	private Importer getImporter(ImportingMethod importingMethod) {
		String importerClassName = importingMethod.getImporterClass();
		try {
			return (Importer) Class.forName(importerClassName).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AssertionError("Failed to resolve Importer by ImporterClassName \"" + importerClassName + "\"");
		}
	}

	public void process(SolvingMethod solvingMethod, Task task) {
		System.out.println("ImportManager: starting data import");
		Set<ImportingMethod> importingMethods = solvingMethod.getImportingMethods();
		System.out.println("ImportManager: " + String.valueOf(importingMethods.size()) + " methods will be ran");
		for( ImportingMethod importingMethod: importingMethods )
			this.getImporter(importingMethod).run(ontologyShell, task);
		System.out.println("ImportManager: finished data import");
	}

}
