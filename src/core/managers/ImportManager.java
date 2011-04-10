package core.managers;

import java.util.Set;

import core.interfaces.Importer;
import core.owl.OWLObjectFactory;
import core.owl.objects.ImportingMethod;
import core.owl.objects.SolvingMethod;
import core.owl.objects.Task;

public class ImportManager {
	
	private final OWLObjectFactory objectFactory;

	public ImportManager(OWLObjectFactory objectFactory) {
		this.objectFactory = objectFactory;
		// TODO Auto-generated constructor stub
	}

	private Importer getImporter(ImportingMethod importingMethod) {
		// TODO
		return null;
	}
	
	public void process(SolvingMethod solvingMethod, Task task) {
		Set<ImportingMethod> importingMethods = solvingMethod.getImportingMethods();
		for( ImportingMethod importingMethod: importingMethods )
			this.getImporter(importingMethod).run(task);
	}
	
}
