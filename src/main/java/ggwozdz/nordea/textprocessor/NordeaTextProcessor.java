package ggwozdz.nordea.textprocessor;

import ggwozdz.nordea.OutputFormat;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

public class NordeaTextProcessor implements TextProcessor {
	private final XMLRenderer xmlRenderer;
	private final CSVRenderer csvRender; 
	
	@Inject
	public NordeaTextProcessor(XMLRenderer xmlRenderer, CSVRenderer csvRender) {		
		this.xmlRenderer = xmlRenderer;
		this.csvRender = csvRender;
	}

	@Override
	public void processText(InputStream is, OutputFormat outputFormat) throws IOException {
		switch(outputFormat){
			case CSV: csvRender.renderToOutputFrom(is, System.out); break;
			case XML: xmlRenderer.renderToOutputFrom(is, System.out); break;				
		}
	}
}
