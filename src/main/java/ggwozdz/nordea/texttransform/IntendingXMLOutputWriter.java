package ggwozdz.nordea.texttransform;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.google.common.base.Strings;

public class IntendingXMLOutputWriter {
	private final XMLStreamWriter xmlWriter;
	private int depthCounter = 0;
		
	private IntendingXMLOutputWriter(OutputStream os) {	
		this.xmlWriter = createWriter(os);
	}
	
	public static IntendingXMLOutputWriter forOutput(OutputStream os){
		return new IntendingXMLOutputWriter(os);
	}
	
	private XMLStreamWriter createWriter(OutputStream os){
		try {
			return XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(os, "utf-8"));
		} catch (UnsupportedEncodingException | XMLStreamException | FactoryConfigurationError e) {
			throw new IllegalStateException("Cannot create XML writer", e);
		}
	}
	
	public IntendingXMLOutputWriter createDoc(){
		try {
			xmlWriter.writeStartDocument();
			return this;
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document:"+e.getMessage(), e);
		}
	}
	
	public void closeDoc(){
		try {
			xmlWriter.writeEndDocument();
			xmlWriter.close();			
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document:"+e.getMessage(), e);
		}
	}
	
	public IntendingXMLOutputWriter startElement(String elementName){
		try {
			this.indentNextElement(depthCounter);
			xmlWriter.writeStartElement(elementName);
			
			++depthCounter;
			return this;
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}	
	}
	
	public IntendingXMLOutputWriter createElementWithText(String elementName, String text){
		try {
			this.indentNextElement(depthCounter);			
			xmlWriter.writeStartElement(elementName);
			
			this.writeText(text);
			xmlWriter.writeEndElement();
			
			return this;
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}	
	}
	
	public IntendingXMLOutputWriter endElement() {
		try {			
			this.indentNextElement(--depthCounter);
			xmlWriter.writeEndElement();			
			return this;
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}	
	}
	
	public IntendingXMLOutputWriter writeText(String text) {
		try {
			xmlWriter.writeCharacters(text);
			return this;
		} catch (XMLStreamException e) {
			throw new IllegalStateException("Error generating XML document",e);
		}	
	}
	
	private void indentNextElement(int depth){
		this.writeText("\n");
		this.writeText(Strings.repeat("   ", depth));
	}
}
