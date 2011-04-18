package core.managers;

import java.util.Set;

import core.interfaces.Importer;
import core.owl.OWLIndividualFactory;
import core.owl.objects.ImportingMethod;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;

public class ImportManager {

	private final OWLIndividualFactory objectFactory;

	public ImportManager(OWLIndividualFactory objectFactory) {
		if (objectFactory == null)
			throw new NullPointerException("objectFactory is null");
		this.objectFactory = objectFactory;
		// TODO Auto-generated constructor stub
	}

	private Importer getImporter(ImportingMethod importingMethod) {
		// TODO
		return null;
	}

	public void process(SolvingMethod solvingMethod, Task task) {
		System.out.println("ImportManager: starting data import");
		Set<ImportingMethod> importingMethods = solvingMethod.getImportingMethods();
		System.out.println("ImportManager: " + String.valueOf(importingMethods.size()) + " methods will be ran");
		for( ImportingMethod importingMethod: importingMethods )
			this.getImporter(importingMethod).run(task);
		System.out.println("ImportManager: finished data import");
	}

}
