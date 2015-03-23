package ggwozdz.nordea.syntax;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public final class WordList implements Comparable<WordList>{
	private static final Joiner JOINER = Joiner.on(",");
	private static final String SEPARATOR_PATTERN = "\\W";

	private final List<String> sortedWords;	
	private final int hashCode;
	
	private WordList(String sentence){
		this.sortedWords = splitSentenceAndSortWords(sentence.trim());
		this.hashCode    = this.sortedWords.hashCode();
	}
	
	public static WordList from(String sentence){
		return new WordList(sentence);
	}
	
	public void forEach(Consumer<String> action) {
        this.sortedWords.forEach(action);
	}
	
	public Stream<String> stream(){
		return this.sortedWords.stream();
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
			.compare(JOINER.join(this.sortedWords), JOINER.join(other.sortedWords))
			.result();
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)			
			.add("sortedWords", this.sortedWords)
			.toString();
	}
}
