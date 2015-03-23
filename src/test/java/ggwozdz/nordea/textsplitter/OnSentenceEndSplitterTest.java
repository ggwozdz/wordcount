package ggwozdz.nordea.textsplitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import ggwozdz.nordea.TestUtils;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnSentenceEndSplitterTest {
	private static final Logger LOG = LoggerFactory.getLogger(OnSentenceEndSplitterTest.class);
	
	@Test
	public void testSplitEmptyText() {
		TestUtils.logTestName(LOG);
		
		String text = "";
		List<String> splits = new OnSentenceEndSplitter().splitLine(text);
		LOG.trace("text {}", text);
		LOG.trace("splits({}) {}", splits.size(), splits);
				
		assertNotNull(splits);
		assertTrue(splits.isEmpty());
	}
	
	@Test
	public void testSplitTextWithSingleSentence() {
		TestUtils.logTestName(LOG);
		String text = "Marry had a little lamb.";
		List<String> splits = new OnSentenceEndSplitter().splitLine(text);
		LOG.trace("text {}", text);
		LOG.trace("splits({}) {}", splits.size(), splits);
		
		assertNotNull(splits);
		assertEquals(1, splits.size());
		assertEquals("Marry had a little lamb.", splits.get(0));
	}
	
	@Test
	public void testSplitTextWithSentencePart() {
		TestUtils.logTestName(LOG);
		String text = "had a little ";
		List<String> splits = new OnSentenceEndSplitter().splitLine(text);
		LOG.trace("text {}", text);
		LOG.trace("splits({}) {}", splits.size(), splits);
		
		assertNotNull(splits);
		assertEquals(1, splits.size());
		assertEquals("had a little ", splits.get(0));
	}
	
	@Test
	public void testSplitTextWithMultipleSentencesEndingWithSentenceTerminator() {
		TestUtils.logTestName(LOG);
		String text = "Marry had a little lamb. Marry had a little lamb!Marry had a little lamb?";
		List<String> splits = new OnSentenceEndSplitter().splitLine(text);
		LOG.trace("text {}", text);
		LOG.trace("splits({}) {}", splits.size(), splits);
		
		assertNotNull(splits);
		assertEquals(3, splits.size());
		assertEquals("Marry had a little lamb.", splits.get(0));
		assertEquals(" Marry had a little lamb!", splits.get(1));
		assertEquals("Marry had a little lamb?", splits.get(2));
	}
	
	@Test
	public void testSplitTextWithMultipleSentencesEngingWithWord() {
		TestUtils.logTestName(LOG);
		String text = "Marry had a little lamb. Marry had a little lamb!Marry had a little";
		List<String> splits = new OnSentenceEndSplitter().splitLine(text);
		LOG.trace("text {}", text);
		LOG.trace("splits({}) {}", splits.size(), splits);
		
		assertNotNull(splits);
		assertEquals(3, splits.size());
		assertEquals("Marry had a little lamb.", splits.get(0));
		assertEquals(" Marry had a little lamb!", splits.get(1));
		assertEquals("Marry had a little", splits.get(2));
	}
}
