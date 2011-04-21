package core.interfaces;

import core.owl.OWLOntologyShell;
import core.owl.objects.Task;

public interface Importer {

	public void run(OWLOntologyShell ontologyShell, Task task);

}
