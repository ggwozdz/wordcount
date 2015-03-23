package ggwozdz.nordea.texttransform;

import ggwozdz.nordea.OutputFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

public class TextTransformer {
	private final InputToXMLTransform xmlRenderer;
	private final InputToCSVTransform csvRenderer;
	
	@Inject
	public TextTransformer(InputToXMLTransform xmlRenderer, InputToCSVTransform csvRenderer) {		
		this.csvRenderer = csvRenderer;
		this.xmlRenderer = xmlRenderer;
	}

	public void processText(InputStream is, OutputStream os, OutputFormat outputFormat) throws IOException {
		switch(outputFormat){
			case CSV: csvRenderer.convertInputToCSV(is, os); break;
			case XML: xmlRenderer.convertInputToXML(is, os); break;				
		}		
	}	
}
