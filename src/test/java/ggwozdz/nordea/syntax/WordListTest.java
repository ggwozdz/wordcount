package ggwozdz.nordea.syntax;

import static org.junit.Assert.*;
import ggwozdz.nordea.Main;
import ggwozdz.nordea.TestUtils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordListTest {
	private static final Logger LOG = LoggerFactory.getLogger(WordListTest.class);
	
	@Test
	public void testWordListFromEmptySentence() {
		TestUtils.logTestName(LOG);
		
		WordList wordList = new WordList("");
		assertNotNull(wordList);
		assertTrue(wordList.getSortedWords().isEmpty());		
	}
	
	@Test
	public void testWordListFromSimpleSentence() {
		TestUtils.logTestName(LOG);
		
		WordList wordList = new WordList("Mary had a little lamb.");
		LOG.trace("wordList: {}", wordList);
		
		assertNotNull(wordList);
		assertEquals(5, wordList.getSortedWords().size());
		assertEquals("a", wordList.getSortedWords().get(0));
		assertEquals("had", wordList.getSortedWords().get(1));
		assertEquals("lamb", wordList.getSortedWords().get(2));
		assertEquals("little", wordList.getSortedWords().get(3));
		assertEquals("Mary", wordList.getSortedWords().get(4));
	}
	
	@Test
	public void testWordListFromCompoundSentence() {
		TestUtils.logTestName(LOG);
		
		WordList wordList = new WordList("\"Peter   called for the wolf   ,  and 'Aesop' came .'");
		assertNotNull(wordList);
		
		LOG.trace("wordList: {}", wordList);
		
		assertEquals(8, wordList.getSortedWords().size());
		assertEquals("Aesop", wordList.getSortedWords().get(0));
		assertEquals("and", wordList.getSortedWords().get(1));
		assertEquals("called", wordList.getSortedWords().get(2));
		assertEquals("came", wordList.getSortedWords().get(3));
		assertEquals("for", wordList.getSortedWords().get(4));
		assertEquals("Peter", wordList.getSortedWords().get(5));
		assertEquals("the", wordList.getSortedWords().get(6));
		assertEquals("wolf", wordList.getSortedWords().get(7));
	}

}
