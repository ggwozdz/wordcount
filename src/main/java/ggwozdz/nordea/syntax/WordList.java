package ggwozdz.nordea.syntax;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public final class WordList implements Comparable<WordList>{
	private static final String SEPARATOR_PATTERN = "\\W";

	private final String       initialSentence;
	private final List<String> sortedWords;
	
	private final int hashCode;
	
	public WordList(String sentence){
		this.initialSentence = sentence.trim();		
		this.sortedWords     = splitSentenceAndSortWords(this.initialSentence);
		
		this.hashCode = this.sortedWords.hashCode();
	}
	
	public String getInitialSentence() {
		return initialSentence;
	}

	public List<String> getSortedWords() {
		return sortedWords;
	}

	private List<String> splitSentenceAndSortWords(String sentence) {
		 List<String> words = Lists.newArrayList(Splitter.onPattern(SEPARATOR_PATTERN)
			.trimResults()
			.omitEmptyStrings()
			.split(sentence));
		 
		 words.sort((x,y) -> x.toLowerCase().compareTo(y.toLowerCase()));
		 return words;
	}
	
	@Override
	public int hashCode() {	
		return this.hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}else if(obj instanceof WordList){
			return Objects.equal(sortedWords, ((WordList)obj).sortedWords);
		}else{
			return false;
		}
	}

	@Override
	public int compareTo(WordList other) {
		return ComparisonChain.start()
			.compare(this.initialSentence, other.initialSentence)
			.result();
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("initialSentence", this.initialSentence)
			.add("sortedWords", this.sortedWords)
			.toString();
	}
}
