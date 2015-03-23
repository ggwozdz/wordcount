package ggwozdz.nordea.texttransform;

import static org.junit.Assert.assertEquals;
import ggwozdz.nordea.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InputToXMLTransformTest {
	private static final Logger LOG = LoggerFactory.getLogger(InputToXMLTransformTest.class); 
	
	@Test
	public void testTransformEmptyTextToXML() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new TextTranformModule());
		InputToXMLTransform inputToXMLTransform = injector.getInstance(InputToXMLTransform.class);
		
		InputStream is = new ByteArrayInputStream("".getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputToXMLTransform.convertInputToXML(is, os);
		
		LOG.trace("output: \n{}", os.toString());
		
		assertEquals(
			"<?xml version=\"1.0\" ?>\n"+
			"<text>\n"+
			"</text>", 
			os.toString());
	}
	
	@Test
	public void testTransformSingleSentenceToXML() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new TextTranformModule());
		InputToXMLTransform inputToXMLTransform = injector.getInstance(InputToXMLTransform.class);
		
		InputStream is = new ByteArrayInputStream("Ala ma kota.".getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputToXMLTransform.convertInputToXML(is, os);
		
		LOG.trace("output: \n{}", os.toString());
		
		assertEquals(
			"<?xml version=\"1.0\" ?>\n"+
			"<text>\n"+
			"    <sentence>\n"+
			"        <word>Ala</word>\n"+
			"        <word>kota</word>\n"+
			"        <word>ma</word>\n"+
			"    </sentence>\n"+
			"</text>", 
			os.toString());
	}
	
	@Test
	public void testTransformMutileSentencesToXML() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new TextTranformModule());
		InputToXMLTransform inputToXMLTransform = injector.getInstance(InputToXMLTransform.class);
		
		InputStream is = new ByteArrayInputStream("Ala ma kota. Foo bar baz.".getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputToXMLTransform.convertInputToXML(is, os);
		
		LOG.trace("output: \n{}", os.toString());
		
		assertEquals(
			"<?xml version=\"1.0\" ?>\n"+
			"<text>\n"+
			"    <sentence>\n"+
			"        <word>Ala</word>\n"+
			"        <word>kota</word>\n"+
			"        <word>ma</word>\n"+
			"    </sentence>\n"+
			"    <sentence>\n"+
			"        <word>bar</word>\n"+
			"        <word>baz</word>\n"+
			"        <word>Foo</word>\n"+
			"    </sentence>\n"+
			"</text>", 
			os.toString());
	}
	
	@Test
	public void testTransformSentenceWithSpecialCharsToXML() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new TextTranformModule());
		InputToXMLTransform inputToXMLTransform = injector.getInstance(InputToXMLTransform.class);
		
		InputStream is = new ByteArrayInputStream("Ala !@ ma !@ kota !@()()(Foo bar baz.".getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputToXMLTransform.convertInputToXML(is, os);
		
		LOG.trace("output: \n{}", os.toString());
		
		assertEquals(
			"<?xml version=\"1.0\" ?>\n"+
			"<text>\n"+
			"    <sentence>\n"+
			"        <word>Ala</word>\n"+
			"        <word>bar</word>\n"+
			"        <word>baz</word>\n"+
			"        <word>Foo</word>\n"+
			"        <word>kota</word>\n"+
			"        <word>ma</word>\n"+			
			"    </sentence>\n"+
			"</text>", 
			os.toString());
	}

}
