package ggwozdz.nordea.textsplitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.inject.Inject;

public class StreamToSentenceSplitter implements InputStreamSplitter {
	private final OnSentenceEndSplitter lineSplitter;
	
	@Inject
	public StreamToSentenceSplitter(OnSentenceEndSplitter lineSplitter) {
		this.lineSplitter = lineSplitter;
	}

	@Override
	public void split(InputStream is, Consumer<String> sentenceConsumer) throws IOException {
		try(Scanner scanner = new Scanner(is, "UTF-8")){		
			StringBuilder currSentence = new StringBuilder();		
			String prevSplit = null;
			while(scanner.hasNext()){
				String currLine             = scanner.nextLine();
				List<String> currLineSplits = lineSplitter.splitLine(currLine);
												
				for (String lineSplit : currLineSplits) {
					boolean newSentence = this.isNewSentence(prevSplit, lineSplit);
					if(newSentence && !currSentence.toString().isEmpty()){
						sentenceConsumer.accept(currSentence.toString());
						currSentence = new StringBuilder();						
					}
					currSentence.append(lineSplit);
					prevSplit = lineSplit;
				}
			}
			
			if(!currSentence.toString().isEmpty()){
				sentenceConsumer.accept(currSentence.toString());
			}			
		}
	}
	
	
	private boolean isNewSentence(String prevSplit, String currSplit){
		//if prev split and curr split get concatenated and do not create a new split it means
		//that curr split is a continuation or prev split
		return prevSplit==null || this.lineSplitter.splitLine(prevSplit+currSplit).size() > 1;
	}

}
