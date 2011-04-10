package core;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
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

import core.owl.OWLOntologyObjectShell;
import core.owl.base.OWLClassObject;
import core.owl.base.OWLIndividualObject;


public class Launcher {

	public static void main(String[] args) {  

		//XStream xstream = new XStream(new DomDriver());
		//String xmlStatus = xstream.toXML("Some Crappy String");
		//String newOne = (String) xstream.fromXML(xmlStatus);
		//System.out.println("We got new status: ");
		//System.out.println(newOne);
		
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
            
            OWLOntologyObjectShell objectOntology = new OWLOntologyObjectShell( man, "http://www.iis.nsk.su/ontologies/main" );
            try {
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
			}
            
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
	
	/*private static void doQueryLoop( DLQueryPrinter dlQueryPrinter ) throws IOException {
		while (true) {
		    // Prompt the user to enter a class expression
		    System.out.println("Please type a class expression in Manchester Syntax");
		    System.out.println("");
		    String classExpression = readInput();
		    // Check for exit condition
		    if(classExpression.equalsIgnoreCase("x")) {
	            break;
	        }
	        dlQueryPrinter.askQuery(classExpression.trim());
	        System.out.println();
	        System.out.println();
	    }	
	}*/
	
	private static String readInput() throws IOException {
	    InputStream is = System.in;
	    InputStreamReader reader = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(reader);
		return br.readLine();

	}
}
