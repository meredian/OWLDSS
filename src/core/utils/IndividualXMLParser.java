package core.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.semanticweb.owlapi.model.IRI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import core.owl.OWLIndividualBuilder;
import core.owl.OWLOntologyObjectShell;

public class IndividualXMLParser {

	/**
	 * Expected format: 
	 * <individual id='234' class='OntologyClassName'> 			// creates new individual
	 * 	<attr name='IntValue' type='int' value='15'/>
	 *  <attr name='Reference' type='object' id='456'/>
	 * </individual>
	 * <individual id='456' iri='http://www.iis.nsk.su/...'>	// uses existing individual
	 * 	<attr name='BackReference' type='object' id='234'/>
	 *  <attr name='ExternalReference' type='object' iri='http://www.iis.nsk.su/...#ExternalObject'/>
	 * </individual>
	 */
	OWLOntologyObjectShell taskContext;
	Map<Integer, IRI> individualsMap = new HashMap<Integer, IRI>();
	
	public IndividualXMLParser(OWLOntologyObjectShell taskContext) {
		this.taskContext = taskContext;
	}
	
	private void parseIndividualAttributes(Element individualElement) throws Exception {
		NodeList childNodes = individualElement.getChildNodes();
		
		String individualId = individualElement.getAttribute("id");
		IRI individualIRI = this.individualsMap.get(Integer.parseInt(individualId));
		OWLIndividualBuilder builder = this.taskContext.getBuilder(individualIRI);
		
		for (int i = 0; i < childNodes.getLength(); ++i) {
			// get the attribute element
			Element attrElement = (Element) childNodes.item(i);
			assert(attrElement.getTagName().equals("attr"));
			
			String attrType = attrElement.getAttribute("type");
			String attrName = attrElement.getAttribute("name");
			
			if (attrType.equals("object")) {
				String iri = attrElement.getAttribute("iri");
				if (iri.isEmpty()) {
					Integer objectId = Integer.parseInt(attrElement.getAttribute("id"));
					builder.addObjectAxiom(attrName, this.individualsMap.get(objectId));
				} else
					builder.addObjectAxiom(attrName, IRI.create(iri));
			} else { 
				String attrValue = attrElement.getAttribute("value");
				if (attrType.equals("string"))
					builder.addAxiom(attrName, attrValue);
				else if (attrType.equals("int"))
					builder.addAxiom(attrName, Integer.parseInt(attrValue));
				else if (attrType.equals("double"))
					builder.addAxiom(attrName, Double.parseDouble(attrValue));
				else if (attrType.equals("boolean"))
					builder.addAxiom(attrName, Boolean.parseBoolean(attrValue));
				else
					throw new Exception("Wrong attribute type met (" + attrType + ")!");
			}
		}
	}
	
	private void parseIndividuals(Document xml) throws Exception {
		NodeList individualNodes = xml.getElementsByTagName("individual");
		for (int i = 0; i < individualNodes.getLength(); ++i) {
			Element individualElement = (Element) individualNodes.item(i);
			String individualClass = individualElement.getAttribute("class");
			String individualIRI = individualElement.getAttribute("iri");
			String individualId = individualElement.getAttribute("id");
			
			if (individualIRI.isEmpty()) {
				IRI newIRI = this.taskContext.createIndividual(individualClass).getIRI();
				this.individualsMap.put(Integer.parseInt(individualId), newIRI);
			} else
				this.individualsMap.put(Integer.parseInt(individualId), IRI.create(individualIRI));
		}
	}
	
	public void parse(String taskXML) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(taskXML.getBytes());
			Document xml = db.parse(stream);
			
			this.parseIndividuals(xml);
			
			NodeList individualNodes = xml.getElementsByTagName("individual");
			for (int i = 0; i < individualNodes.getLength(); ++i) {
				Element individualElement = (Element) individualNodes.item(i);
				this.parseIndividualAttributes(individualElement);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
}
