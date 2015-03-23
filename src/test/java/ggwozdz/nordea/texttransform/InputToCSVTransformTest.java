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

public class InputToCSVTransformTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(InputToCSVTransformTest.class); 

	@Test
	public void testTransformEmptyTextToCSV() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new TextTranformModule());
		InputToCSVTransform inputToCSVTransform = injector.getInstance(InputToCSVTransform.class);
		
		InputStream is = new ByteArrayInputStream("".getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputToCSVTransform.convertInputToCSV(is, os);
		
		LOG.trace("output: \n{}", os.toString());
		
		assertEquals("\n", os.toString());
	}
	
	@Test
	public void testTransformSingleSentenceToCSV() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new TextTranformModule());
		InputToCSVTransform inputToCSVTransform = injector.getInstance(InputToCSVTransform.class);
		
		InputStream is = new ByteArrayInputStream("Ala ma kota.".getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputToCSVTransform.convertInputToCSV(is, os);
		
		LOG.trace("output: \n{}", os.toString());
		
		assertEquals(
			", Word 1, Word 2, Word 3\n"+
			"Sentence 1, Ala,kota,ma\n", 
			os.toString());		
	}
	
	@Test
	public void testTransformMutileSentencesToCSV() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new TextTranformModule());
		InputToCSVTransform inputToCSVTransform = injector.getInstance(InputToCSVTransform.class);
		
		InputStream is = new ByteArrayInputStream("Ala ma kota. Foo bar baz xyz.".getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputToCSVTransform.convertInputToCSV(is, os);
		
		LOG.trace("output: \n{}", os.toString());
		
		assertEquals(
			", Word 1, Word 2, Word 3, Word 4\n"+
			"Sentence 1, Ala,kota,ma\n"+
			"Sentence 2, bar,baz,Foo,xyz\n",
			os.toString());
	}
	
	@Test
	public void testTransformSentenceWithSpecialCharsToCSV() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new TextTranformModule());
		InputToCSVTransform inputToCSVTransform = injector.getInstance(InputToCSVTransform.class);
		
		InputStream is = new ByteArrayInputStream("Ala !@ ma !@ kota !@()()(Foo bar baz.".getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		inputToCSVTransform.convertInputToCSV(is, os);
		
		LOG.trace("output: \n{}", os.toString());
		
		assertEquals(
			", Word 1, Word 2, Word 3, Word 4, Word 5, Word 6\n"+
			"Sentence 1, Ala,bar,baz,Foo,kota,ma\n",			 
			os.toString());
	}

}
