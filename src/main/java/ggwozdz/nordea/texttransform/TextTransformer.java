package ggwozdz.nordea.texttransform;

import ggwozdz.nordea.OutputFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

public class TextTransformer {
	private final InputToXMLTransform toXMLTransform;
	private final InputToCSVTransform toCSVTransform;
	
	@Inject
	public TextTransformer(InputToXMLTransform xmlRenderer, InputToCSVTransform csvRenderer) {		
		this.toCSVTransform = csvRenderer;
		this.toXMLTransform = xmlRenderer;
	}

	public void processText(InputStream input, OutputStream output, OutputFormat outputFormat) throws IOException {
		switch(outputFormat){
			case CSV: toCSVTransform.convertInputToCSV(input, output); break;
			case XML: toXMLTransform.convertInputToXML(input, output); break;				
		}		
	}	
}
