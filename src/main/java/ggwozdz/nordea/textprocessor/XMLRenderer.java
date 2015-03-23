package ggwozdz.nordea.textprocessor;

import ggwozdz.nordea.syntax.WordList;
import ggwozdz.nordea.textsplitter.InputStreamSplitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class XMLRenderer {
	private static final Logger LOG = LoggerFactory.getLogger(CSVRenderer.class);
	private final InputStreamSplitter splitter;
	
	@Inject
	public XMLRenderer(InputStreamSplitter splitter) {
		this.splitter = splitter;
	}
	
	public void renderToOutputFrom(InputStream is, OutputStream out) throws IOException{
		XMLStreamWriter xmlWriter = createWriter(out);
		
		writeDocWithRootElement(xmlWriter);
		AtomicInteger sentenceIndex = new AtomicInteger();
		this.splitter.split(is, sentence -> {
			
			if(sentenceIndex.incrementAndGet() % 1000 == 0){
				LOG.trace("Processed {} sentences", sentenceIndex);
			}
			writeText(xmlWriter, "\n   ");
			startElement(xmlWriter, "sentence");			
			new WordList(sentence).getSortedWords().forEach( word -> {
				writeText(xmlWriter, "\n       ");
				createElementWithText(xmlWriter, "word", word);
			});
			writeText(xmlWriter, "\n   ");
			endElement(xmlWriter);
			
		});
		finalizeXMLDoc(xmlWriter);
		
	}
	private XMLStreamWriter createWriter(OutputStream os){
		try {
			return XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(os, "utf-8"));
		} catch (UnsupportedEncodingException | XMLStreamException | FactoryConfigurationError e) {
			throw new IllegalStateException("Cannot create XML writer", e);
		}
	}

	private void writeDocWithRootElement(XMLStreamWriter xmlWriter) {
		try{
			xmlWriter.writeStartDocument();
			writeText(xmlWriter, "\n");
			xmlWriter.writeStartElement("text");
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}	
	}
	
	private void startElement(XMLStreamWriter xmlWriter, String name) {
		try {
			xmlWriter.writeStartElement(name);
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}	
	}
	
	private void writeText(XMLStreamWriter xmlWriter, String text) {
		try {
			xmlWriter.writeCharacters(text);
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}	
	}
	
	private void endElement(XMLStreamWriter xmlWriter) {
		try {
			xmlWriter.writeEndElement();	
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}	
	}
	
	private void createElementWithText(XMLStreamWriter xmlWriter, String element, String text){
		this.startElement(xmlWriter, element);
		this.writeText(xmlWriter, text);
		this.endElement(xmlWriter);
	}
	
	private void finalizeXMLDoc(XMLStreamWriter xmlWriter) {
		try{
			writeText(xmlWriter, "\n");
			xmlWriter.writeEndElement();			
			xmlWriter.writeEndDocument();
			xmlWriter.close();
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}
	}
}
