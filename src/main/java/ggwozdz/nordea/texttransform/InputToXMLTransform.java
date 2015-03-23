package ggwozdz.nordea.texttransform;

import ggwozdz.nordea.syntax.WordList;
import ggwozdz.nordea.textsplitter.InputStreamSplitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

class InputToXMLTransform {
	private final InputStreamSplitter splitter;
	
	@Inject
	public InputToXMLTransform(InputStreamSplitter splitter) {
		this.splitter = splitter;
	}
	
	public void convertInputToXML(InputStream is, OutputStream os) throws IOException{
		IntendingXMLOutputWriter xmlWriter = IntendingXMLOutputWriter.forOutput(os);
		
		this.initDoc(xmlWriter);
			
		this.splitter.split(is, sentence -> {
			xmlWriter.startElement("sentence");			
			WordList.from(sentence).forEach( word -> xmlWriter.createElementWithText("word", word));			
			xmlWriter.endElement();			
		});
		
		this.finalizeDoc(xmlWriter);
			
	}

	private void initDoc(IntendingXMLOutputWriter xmlWriter) {
		xmlWriter.createDoc().startElement("text");
	}
	
	private void finalizeDoc(IntendingXMLOutputWriter xmlWriter){
		xmlWriter.endElement().closeDoc();
	}
}
