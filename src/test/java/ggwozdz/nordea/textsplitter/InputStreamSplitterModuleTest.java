package ggwozdz.nordea.textsplitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ggwozdz.nordea.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class InputStreamSplitterModuleTest {
	private static final Logger LOG = LoggerFactory.getLogger(InputStreamSplitterModuleTest.class);
	
	@Test
	public void testSplitEmptyText() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new InputStreamSplitterModule());
		InputStreamSplitter streamSplitter = injector.getInstance(InputStreamSplitter.class);
		
		AggregatingConsumer consumer = new AggregatingConsumer();
		streamSplitter.split(new ByteArrayInputStream("".getBytes()), consumer);
		LOG.trace("data {}: {}", consumer.data.size(), consumer.data);
		
		assertTrue(consumer.getData().isEmpty());		
	}
	
	
	@Test
	public void testSplitTextWithSingleSentence() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new InputStreamSplitterModule());
		InputStreamSplitter streamSplitter = injector.getInstance(InputStreamSplitter.class);
		
		String text = "   Marry had a little lamb.\t\n";
		
		AggregatingConsumer consumer = new AggregatingConsumer();
		streamSplitter.split(new ByteArrayInputStream(text.getBytes()), consumer);
		
		LOG.trace("data {}: {}", consumer.data.size(), consumer.data);
		
		assertEquals("   Marry had a little lamb.\t", consumer.getData().get(0));		
	}
	
	@Test
	public void testSplitTextWithMultipleSentecesInOneLine() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new InputStreamSplitterModule());
		InputStreamSplitter streamSplitter = injector.getInstance(InputStreamSplitter.class);
		
		String text = "“Mary had a little lamb. Peter called for the wolf , and Aesop came.Cinderella likes shoes.”";
		
		AggregatingConsumer consumer = new AggregatingConsumer();
		streamSplitter.split(new ByteArrayInputStream(text.getBytes()), consumer);
		
		LOG.trace("data {}: {}", consumer.data.size(), consumer.data);
		
		assertEquals("“Mary had a little lamb.", consumer.getData().get(0));
		assertEquals(" Peter called for the wolf , and Aesop came.", consumer.getData().get(1));
		assertEquals("Cinderella likes shoes.”", consumer.getData().get(2));
	}
	
	@Test
	public void testSplitTextWithMultipleSentecesInOneLineWithNumbers() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new InputStreamSplitterModule());
		InputStreamSplitter streamSplitter = injector.getInstance(InputStreamSplitter.class);
		
		String text = "Ms. Mary had a little lamb! Peter called for the wolf 12.1234, bear etc. , and Aesop came?\tCinderella likes shoes.";
		
		AggregatingConsumer consumer = new AggregatingConsumer();
		streamSplitter.split(new ByteArrayInputStream(text.getBytes()), consumer);
		
		LOG.trace("data {}: {}", consumer.data.size(), consumer.data);
		
		assertEquals("Ms. Mary had a little lamb!", consumer.getData().get(0));
		assertEquals(" Peter called for the wolf 12.1234, bear etc. , and Aesop came?", consumer.getData().get(1));
		assertEquals("\tCinderella likes shoes.", consumer.getData().get(2));
	}
	
	@Test
	public void testSplitTextWithSenteceSpanningMutipleLines() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new InputStreamSplitterModule());
		InputStreamSplitter streamSplitter = injector.getInstance(InputStreamSplitter.class);
		
		String text = "“Mary had a little lamb \n and Peter called for \n the wolf , and Aesop came \n and \nCinderella likes shoes.”";
		
		AggregatingConsumer consumer = new AggregatingConsumer();
		streamSplitter.split(new ByteArrayInputStream(text.getBytes()), consumer);
		
		LOG.trace("data {}: {}", consumer.data.size(), consumer.data);
		
		assertEquals("“Mary had a little lamb  and Peter called for  the wolf , and Aesop came  and Cinderella likes shoes.”", consumer.getData().get(0));		
	}
	
	@Test
	public void testSplitTextWithOneSenteceInEachline() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new InputStreamSplitterModule());
		InputStreamSplitter streamSplitter = injector.getInstance(InputStreamSplitter.class);
		
		String text = "Mary had a little lamb.\nPeter called for the wolf , and Aesop came?\nFoo bar baz!";
		
		AggregatingConsumer consumer = new AggregatingConsumer();
		streamSplitter.split(new ByteArrayInputStream(text.getBytes()), consumer);
		
		LOG.trace("data {}: {}", consumer.data.size(), consumer.data);
		
		assertEquals("Mary had a little lamb.", consumer.getData().get(0));	
		assertEquals("Peter called for the wolf , and Aesop came?", consumer.getData().get(1));	
		assertEquals("Foo bar baz!", consumer.getData().get(2));
	}
	
	@Test
	public void testSplitTextWithSenteceSplitAcrossTwoLinesInEachline() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new InputStreamSplitterModule());
		InputStreamSplitter streamSplitter = injector.getInstance(InputStreamSplitter.class);
		
		String text = "Mary had a little lamb.\nPeter called for the wolf , \nand Aesop came?\nFoo bar baz!";
		
		AggregatingConsumer consumer = new AggregatingConsumer();
		streamSplitter.split(new ByteArrayInputStream(text.getBytes()), consumer);
		
		LOG.trace("data {}: {}", consumer.data.size(), consumer.data);
		
		assertEquals("Mary had a little lamb.", consumer.getData().get(0));	
		assertEquals("Peter called for the wolf , and Aesop came?", consumer.getData().get(1));	
		assertEquals("Foo bar baz!", consumer.getData().get(2));
	}
	
	@Test
	public void testSplitRealText() throws IOException {
		TestUtils.logTestName(LOG);
		
		Injector injector = Guice.createInjector(new InputStreamSplitterModule());
		InputStreamSplitter streamSplitter = injector.getInstance(InputStreamSplitter.class);
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("small.txt");
				
		AggregatingConsumer consumer = new AggregatingConsumer();
		streamSplitter.split(is, consumer);
		
		LOG.trace("data {}: {}", consumer.data.size(), consumer.data);		
	}
	
	private static final class AggregatingConsumer implements Consumer<String>{
		private final List<String> data = Lists.newArrayList();
		
		@Override
		public void accept(String t) {
			data.add(t);			
		}

		public List<String> getData() {
			return data;
		}
	}

}
