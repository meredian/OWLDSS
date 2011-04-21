package implementation.importers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;

import core.interfaces.Importer;
import core.owl.OWLIndividualBuilder;
import core.owl.OWLIndividualReader;
import core.owl.OWLOntologyShell;
import core.owl.objects.Task;
import data.SQLBackEnd;
import data.SQLiteBackEnd;

public class PumpEfficiencyDeviationRowImporter implements Importer {

	private static final String ROW_CLASS = "PumpEfficiencyDeviationRow";
	private static final String ROW_ATTR_VALUE = "RowValue";

	private static final String PUMP_CLASS = "Pump";
	private static final String PUMP_ATTR_ID = "Id";
	private static final String PUMP_ATTR_ROW = "HasPumpEfficiencyDeviationRow";
	private SQLBackEnd backend;

	@Override
	public void run(OWLOntologyShell ontologyShell, Task task) {
		backend = new SQLiteBackEnd();
		Set<IRI> pumps = task.getInputObjectsByClass(PUMP_CLASS);
		for (IRI pump : pumps) {
			OWLIndividualReader pumpReader = ontologyShell.getReader(pump);
			OWLIndividualBuilder pumpBuilder = ontologyShell.getBuilder(pump);
			OWLIndividualBuilder rowBuilder = ontologyShell.createIndividual(ROW_CLASS);
			int rowId = pumpReader.getIntegerValue(PUMP_ATTR_ID);
			try {
				ResultSet pumpData = backend.getDataForClassFiltered(PUMP_CLASS,
									new String[]{PUMP_ATTR_ID}, new String[]{"" + rowId});
				pumpData.getString("efficiency_deviation_row");
				pumpData.next();

				rowBuilder.addAxiom(ROW_ATTR_VALUE, pumpData.getString("efficiency_deviation_row"));
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AssertionError("PumpEfficiencyDeviationRowImporter: Can't load data for " +
						PUMP_CLASS + " from SQL");
			}

			pumpBuilder.addObjectAxiom(PUMP_ATTR_ROW, rowBuilder.getIRI());

			task.addImport(rowBuilder.getIRI());
		}
		backend.close();
	}

}
