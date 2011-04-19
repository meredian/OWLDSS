package implementation.renderers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLLiteral;

import core.interfaces.Renderer;
import core.owl.objects.Presentation;
import core.owl.objects.PresentationMethod;

public class FileOutputRenderer implements Renderer {

	private final String ATTRIBUTE_OUTPUT_FILE_PATH = "OutputFilePath";

	@Override
	public void run(Presentation presentation, PresentationMethod presentationMethod) throws Exception {
		String value = presentation.getLiteralValue().getLiteral();
		if (value == null) {
			System.err.println( "FileOutputRenderer: presentation object not supported" );
			return;
		}

		Set<?> filePathValues = presentationMethod.getAllAttributes().get(ATTRIBUTE_OUTPUT_FILE_PATH);
		if (filePathValues.size() != 1) {
			System.err.println( "FileOutputRenderer: could not read output file path from ontology; rendering aborted" );
			return;
		}

		OWLLiteral literal = (OWLLiteral) filePathValues.iterator().next();
		String filePath = literal.getLiteral();
		if (filePath == null) {
			System.err.println( "FileOutputRenderer: could not read output file path from ontology; rendering aborted" );
			return;
		}

		File file = new File(filePath);
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(value.getBytes());
		stream.close();
		System.out.println( "FileOutputRenderer: successfully finished writing output file " + filePath );
	}
}
