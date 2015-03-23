package ggwozdz.nordea.textsplitter;

import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

class OnSentenceEndSplitter {
	//looks for sentence punctuation followed by a capital letter
	//does not split on abbreviations (short words ending with. like etc.)
	private final Pattern SENTENCE_END_REGEX = Pattern.compile(
			"(?<!\\b\\p{Upper}\\w{0,2})(?=[.?!]\\s*\\p{Upper})[.?!]");
	private final Splitter ON_SENTENCE_END_SPLITTER = Splitter.on(SENTENCE_END_REGEX)			
			.omitEmptyStrings();
	
	List<String> splitLine(String text){		
		List<String> lineSplits = ON_SENTENCE_END_SPLITTER.splitToList(text);
		if(lineSplits.size() <= 1){
			return lineSplits;
		}else{
			//all but last items will have sentence terminator char removed
			//it need to be restored for much easier sentence building (multiple line case)
			return restoreSentenceTerminators(text, lineSplits);
		}	
	}
	
	private List<String> restoreSentenceTerminators(String text, List<String> lineSplits){
		
		//go through 0-(n-1) elements in the splits to extract following pieces
		//text [SPLIT1][terminator1][SPLIT2][terminator2]...[SPLITn-1][terminator_n-1]
		//each split with terminator has a range of <index split n, index of split n+1)				
		List<String> lineSplitsWithSentenceTerminator = Lists.newArrayList();
		int searchStartIndex = 0;		
		for(int i=0; i<lineSplits.size()-1; ++i){
			int startIndex = text.indexOf(lineSplits.get(i), searchStartIndex);
			int endIndex   = text.indexOf(lineSplits.get(i+1), startIndex+lineSplits.get(i).length());
			lineSplitsWithSentenceTerminator.add(text.substring(startIndex, endIndex));
			searchStartIndex = endIndex;
		}
		
		//previous items were recoverd - just add last one
		lineSplitsWithSentenceTerminator.add(Iterables.getLast(lineSplits));
		
		return lineSplitsWithSentenceTerminator;
	}
}
