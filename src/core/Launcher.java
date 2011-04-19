package core;

import implementation.importers.MySqlBaseImporter;
import implementation.solvers.SempSolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.semanticweb.owlapi.vocab.OWLFacet;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import core.interfaces.Solver;
import core.owl.OWLOntologyShell;
import core.repository.MethodSignature;
import core.repository.SolverRepository;
import core.supervisor.TaskProcessor;
import core.utils.ConfigStorage;
import core.utils.IndividualXMLParser;
import core.utils.MethodSelectionMode;

public class Launcher {

	public static void main(String[] args) {
		try {
			repositoryTests();
			initSolvers();
			//initMySqlConnection();
			testSempTask();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initMySqlConnection(){
		System.out.println("START MYSQL INIT");
		MySqlBaseImporter.connect();
		System.out.println("FINISH MYSQL INIT");
	}

	private static void initSolvers(){
		System.out.println("START SEMPSOLVER INIT");
		SolverRepository repo = new SolverRepository();
		Solver sempSolver = new SempSolver();
		MethodSignature efficiencyTrendAnalysis = new MethodSignature("EfficiencyTrendAnalysis");
		efficiencyTrendAnalysis.setParam("MODULE_PATH", "~/.wine/drive_c/semp/modules/EfficiencyTrendAnalysis/");
		efficiencyTrendAnalysis.setParam("MODULE_LAUNCHER", "EfficiencyTrendAnalysis_Launcher.pm");
		efficiencyTrendAnalysis.setParam("MODULE_DATA_INPUT", "/home/where-is-s/.wine/drive_c/semp/modules/EfficiencyTrendAnalysis/EfficiencyTrendAnalysis_CreateData.pm");
		efficiencyTrendAnalysis.setParam("MODULE_CREATE_DATA_HEADER",
			"uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;\r\n" +
			"uses Global_Ontology;\r\n" +
			"rule CreateData\r\n" +
			"=>\r\n" +
			"new\r\n"
		);
		efficiencyTrendAnalysis.setParam("MODULE_CREATE_DATA_TAIL","end;\r\n");
		sempSolver.addMethod(efficiencyTrendAnalysis);
		MethodSignature rowTendencyAnalysis = new MethodSignature("RowTendencyAnalysis");
		rowTendencyAnalysis.setParam("MODULE_PATH", "~/.wine/drive_c/semp/modules/RowTendencyAnalysis/");
		rowTendencyAnalysis.setParam("MODULE_LAUNCHER", "RowTendencyAnalysis_Launcher.pm");
		rowTendencyAnalysis.setParam("MODULE_DATA_INPUT", "/home/where-is-s/.wine/drive_c/semp/modules/RowTendencyAnalysis/RowTendencyAnalysis_CreateData.pm");
		rowTendencyAnalysis.setParam("MODULE_CREATE_DATA_HEADER",
			"uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;\r\n" +
			"uses Global_Ontology;\r\n" +
			"rule CreateData\r\n" +
			"=>\r\n" +
			"new\r\n"
		);
		rowTendencyAnalysis.setParam("MODULE_CREATE_DATA_TAIL","end;\r\n");
		sempSolver.addMethod(rowTendencyAnalysis);
		MethodSignature pumpTendencyAnalysis = new MethodSignature("PumpTendencyAnalysis");
		pumpTendencyAnalysis.setParam("MODULE_PATH", "~/.wine/drive_c/semp/modules/PumpTendencyAnalysis/");
		pumpTendencyAnalysis.setParam("MODULE_LAUNCHER", "PumpTendencyAnalysis_Launcher.pm");
		pumpTendencyAnalysis.setParam("MODULE_DATA_INPUT", "/home/where-is-s/.wine/drive_c/semp/modules/PumpTendencyAnalysis/PumpTendencyAnalysis_CreateData.pm");
		pumpTendencyAnalysis.setParam("MODULE_CREATE_DATA_HEADER",
			"uses CATNemNumbers, CATSempTypes, CATSempProductions, CATSempContainers;\r\n" +
			"uses Global_Ontology;\r\n" +
			"rule CreateData\r\n" +
			"=>\r\n" +
			"new\r\n"
		);
		pumpTendencyAnalysis.setParam("MODULE_CREATE_DATA_TAIL","end;\r\n");
		sempSolver.addMethod(pumpTendencyAnalysis);
		repo.addSolver(sempSolver);
		repo.saveToStorage();
		System.out.println("FINISH SEMPSOLVER INIT");
	}

	private static void storageTests() {
		System.out.println("STARING STORAGE TEST");
		ConfigStorage storage = new ConfigStorage("storage");
		System.out.println(storage.readConfig("SomeSolver"));
		storage.writeConfig("SomeSolver", "This is initial config string");
		System.out.println(storage.readConfig("SomeSolver"));
		storage.writeConfig("SomeSolver", "This is initial config string");
		System.out.println(storage.readConfig("SomeSolver"));
		System.out.println("END STORAGE TEST");
	}

	private static void repositoryTests() {
		System.out.println("STARING REPOSITORY TEST");
		SolverRepository repo = new SolverRepository();
		List<String> list = repo.getSolverListFromStorage();
		System.out.println("END REPOSITORY TEST");
	}

	private static void XStreamStartup() {
		XStream xstream = new XStream(new DomDriver());
		String xmlStatus = xstream.toXML("Some Crappy String");
		String newOne = (String) xstream.fromXML(xmlStatus);
		System.out.println("We got new status: ");
		System.out.println(newOne);
	}

	private static void testRowInvertTask() throws OWLOntologyCreationException {
		String testXML =
			"<individuals>" +
				"<individual class='RowInvertTask' id='0'>" +
					"<attr name='HasInput' type='object' id='1'/>" +
				"</individual>" +
				"<individual class='Row' id='1'>" +
					"<attr name='RowValue' type='string' value='1 2 63 123 3 9'/>" +
				"</individual>" +
			"</individuals>";

		TaskProcessor processor = new TaskProcessor(
			new File("ontologies/Ontology1.owl").getAbsolutePath(),
			"http://www.iis.nsk.su/ontologies/main.owl",
			new MethodSelectionMode()
		);
		processor.onTaskReceived(testXML);
		processor.cancel(); // process just one iteration
		processor.process();
	}

	private static void testTaskContextCreation() throws OWLOntologyCreationException {
		String ontologyAddress = "http://www.iis.nsk.su/ontologies/main.owl";
		OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
		ontologyManager.loadOntologyFromOntologyDocument(new File("ontologies/Ontology1.owl"));

		OWLOntologyShell ontologyShell = new OWLOntologyShell(ontologyManager, ontologyAddress);
		String testXML =
			"<individuals>" +
				"<individual class='PumpAnalysisTask' id='0'>" +
					"<attr name='HasPump' type='object' id='1'/>" +
				"</individual>" +
				"<individual class='Pump' id='1'>" +
					"<attr name='Id' type='int' value='5'/>" +
				"</individual>" +
			"</individuals>";
		IndividualXMLParser parser = new IndividualXMLParser(ontologyShell, testXML);
		ontologyShell.dumpOntology();
	}

	private static void testSempTask() throws OWLOntologyCreationException {
		System.out.println("STARING SEMP SOLVER TEST");
		String testXML =
			"<individuals>" +
				"<individual class='PumpAnalysisTask' id='0'>" +
					"<attr name='HasInputPump' type='object' id='4'/>" +
					"<attr name='HasImport' type='object' id='1'/>" +
					"<attr name='HasImport' type='object' id='2'/>" +
					"<attr name='HasImport' type='object' id='3'/>" +
				"</individual>" +
				"<individual class='PumpReferenceData' id='1'>" +
				"</individual>" +
				"<individual class='OAGAReferenceData' id='2'>" +
					"<attr name='MaxEfficiencyDeviation' type='double' value='20'/>" +
				"</individual>" +
				"<individual class='PumpEfficiencyDeviationRow' id='3'>" +
					//"<attr name='RowValue' type='string' value='real[0.19,0.19,0.18,0.18,0.20,0.19,0.20,0.19,0.20,0.19,0.19,0.20]'/>" + // OK
					//"<attr name='RowValue' type='string' value='real[0.27,0.26,0.25,0.25,0.25,0.23,0.22,0.20,0.19,0.18,0.16,0.15]'/>" + // big problem!
					//"<attr name='RowValue' type='string' value='real[0.05,0.06,0.08,0.09,0.10,0.12,0.13,0.15,0.15,0.15,0.16,0.17]'/>" + // OK!
					"<attr name='RowValue' type='string' value='real[0.12,0.11,0.10,0.10,0.10,0.08,0.07,0.05,0.04,0.03,0.01,0.00]'/>" + // warning in 7 hours
				"</individual>" +
				"<individual class='Pump' id='4'>" +
					"<attr name='HasPumpEfficiencyDeviationRow' type='object' id='3'/>" +
					"<attr name='HasPumpReferenceData' type='object' id='1'/>" +
				"</individual>" +
			"</individuals>";

		TaskProcessor processor = new TaskProcessor(
			new File("ontologies/Ontology1.owl").getAbsolutePath(),
			"http://www.iis.nsk.su/ontologies/main.owl",
			new MethodSelectionMode(false, true, true)
		);
		processor.onTaskReceived(testXML);
		processor.cancel(); // process just one iteration
		processor.process();
		System.out.println("END SEMP SOLVER TEST");
	}

	private static void simpleReasonerTest() throws OWLOntologyCreationException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		String ontologyAddress = "http://www.iis.nsk.su/ontologies/main.owl";
		OWLOntology ont = man.createOntology(IRI.create(ontologyAddress));

		OWLDataFactory dataFactory = man.getOWLDataFactory();

		OWLClass taskClass = dataFactory.getOWLClass(IRI.create(ontologyAddress + "#Task"));
		OWLClass subtaskClass = dataFactory.getOWLClass(IRI.create(ontologyAddress + "#SubTask"));
		man.addAxiom(ont, dataFactory.getOWLSubClassOfAxiom(subtaskClass, taskClass));

		OWLNamedIndividual individual = dataFactory.getOWLNamedIndividual(IRI.create(ontologyAddress + "#SubTask_0"));
		man.addAxiom(ont, dataFactory.getOWLClassAssertionAxiom(subtaskClass, individual));

		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration( progressMonitor );
		OWLReasoner reasoner = reasonerFactory.createReasoner( ont, config );

		NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(taskClass, false);
		assert(!individuals.isEmpty());
	}

	private static void OWLStartup() {
		try {
			OWLOntologyManager man = OWLManager.createOWLOntologyManager();

			String base = "http://www.iis.nsk.su/ontologies/main";

			OWLOntology ont = man.createOntology(IRI.create(base));

			OWLDataFactory dataFactory = man.getOWLDataFactory();

			OWLClass abstractTaskClass = dataFactory.getOWLClass( IRI.create(base + "#AbstractTask" ) );
			OWLClass concreteTaskClass = dataFactory.getOWLClass( IRI.create(base + "#ConcreteTask" ) );

			man.addAxiom(ont, dataFactory.getOWLSubClassOfAxiom( concreteTaskClass, abstractTaskClass ) );

			OWLDataProperty taskName = dataFactory.getOWLDataProperty( IRI.create(base + "#TaskName" ) );
			OWLDataProperty taskPriority = dataFactory.getOWLDataProperty( IRI.create(base + "#TaskPriority" ) );

			man.addAxiom( ont, dataFactory.getOWLDataPropertyDomainAxiom( taskName, abstractTaskClass ) );
			man.addAxiom( ont, dataFactory.getOWLDataPropertyRangeAxiom( taskName, dataFactory.getRDFPlainLiteral() ) );
			//man.addAxiom( ont, dataFactory.getOWLSubClassOfAxiom( abstractTaskClass, dataFactory.getOWLDataExactCardinality( 1, taskName ) ) );

			man.addAxiom( ont, dataFactory.getOWLDataPropertyDomainAxiom( taskPriority, abstractTaskClass ) );

			OWLLiteral oneLiteral = dataFactory.getOWLLiteral(1);
			OWLDatatype integer = dataFactory.getIntegerOWLDatatype();
			OWLDatatypeRestriction integerMin1 = dataFactory.getOWLDatatypeRestriction( integer, OWLFacet.MIN_INCLUSIVE, oneLiteral );
			man.addAxiom( ont, dataFactory.getOWLDataPropertyRangeAxiom( taskPriority, integerMin1 ) );

			OWLNamedIndividual concreteTask = dataFactory.getOWLNamedIndividual( IRI.create(base + "#FirstTask" ) );
			man.addAxiom( ont, dataFactory.getOWLClassAssertionAxiom( concreteTaskClass, concreteTask ) );
			man.addAxiom( ont, dataFactory.getOWLDataPropertyAssertionAxiom( taskName, concreteTask, "First task" ) );

			man.addAxiom( ont, dataFactory.getOWLDataPropertyAssertionAxiom( taskPriority, concreteTask, 5 ) );

			OWLOntologyShell objectOntology = new OWLOntologyShell( man, "http://www.iis.nsk.su/ontologies/main" );
			/*try {
				OWLClassObject classObject = objectOntology.getClassObject( "ConcreteTask" );
				OWLIndividualObject individualObject = objectOntology.createIndividual( classObject );
				individualObject.getPropertyByName( "TaskPriority" ).setIntegerValue( 19 );
				System.out.println( individualObject.getPropertyByName( "TaskPriority" ).getIntegerValue() );

				OWLClassObject abstractClassObject = objectOntology.getClassObject( "AbstractTask" );
				Collection< OWLIndividualObject > inds = abstractClassObject.getIndividuals( false );
				for( OWLIndividualObject ind: inds ) {
					System.out.println( "Priority: " + ind.getPropertyByName( "TaskPriority" ).getIntegerValue() );
				}
				//classObject.getDirectParent();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			// Save our ontology
			man.saveOntology( ont, IRI.create( "file:/home/where-is-s/workspace/temp/example.owl" ) );

			OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
			ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			OWLReasonerConfiguration config = new SimpleConfiguration( progressMonitor );
			OWLReasoner reasoner = reasonerFactory.createReasoner( ont, config );
			reasoner.precomputeInferences();

			boolean consistent = reasoner.isConsistent();
			System.out.println( "Consistent: " + consistent + "\n" );

			NodeSet<OWLNamedIndividual> set = reasoner.getInstances( abstractTaskClass, false );
			System.out.println( "Count of tasks: " + set.getNodes().size() + "\n" );
			for( Node<OWLNamedIndividual> node: set.getNodes() ) {
				Set<OWLLiteral> literals = node.getRepresentativeElement().getDataPropertyValues( taskName, ont );
				for( OWLLiteral literal: literals ) {
					System.out.println( "Name: " + literal.getLiteral() );
				}
				literals = node.getRepresentativeElement().getDataPropertyValues( taskPriority, ont );
				for( OWLLiteral literal: literals ) {
					System.out.println( "Priority: " + literal.parseInteger() );
				}
			}

			ShortFormProvider shortFormProvider = new SimpleShortFormProvider();

			/*            DLQueryPrinter dlQueryPrinter = new DLQueryPrinter( reasoner, shortFormProvider );

            doQueryLoop( dlQueryPrinter );*/

		}
		catch ( OWLOntologyCreationException e ) {
			System.out.println( "Could not create ontology: " + e.getMessage() );
		}
		catch ( OWLOntologyStorageException e ) {
			System.out.println( "Could not save ontology: " + e.getMessage() );
		}
	}

	private static String readInput() throws IOException {
	    InputStream is = System.in;
	    InputStreamReader reader = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(reader);
		return br.readLine();

	}
}
