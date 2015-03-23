package ggwozdz.nordea.texttransform;

import ggwozdz.nordea.syntax.WordList;
import ggwozdz.nordea.textsplitter.InputStreamSplitter;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public final class InputToCSVTransform {
	private final InputStreamSplitter splitter;
	
	@Inject
	public InputToCSVTransform(InputStreamSplitter splitter) {
		this.splitter = splitter;
	}
	
	public void convertInputToCSV(InputStream is, OutputStream os) throws IOException {
		// need to write to tmp file first not to loose data
		// then scan through all sentences and determine the one with max no of words to generate header
		// finally generate header and copy file to output
		File tmpFile  = this.createTmpFile();
		int maxWords  = this.countMaxWordsAndPreprocessData(is, tmpFile);
		String header = this.generateHeader(maxWords);
		
		ByteStreams.copy(new ByteArrayInputStream(header.getBytes()), os);
		ByteStreams.copy(new FileInputStream(tmpFile), os);		
	}
	
	private int countMaxWordsAndPreprocessData(InputStream is, File tmpFile) throws IOException{
		
		AtomicInteger maxWords = new AtomicInteger();
		AtomicInteger sentenceIndex = new AtomicInteger();
		
		try(PrintWriter writer = createTmpFileWriter(tmpFile)){
			this.splitter.split(is, sentence -> {
								
				List<String> sortedWords = WordList.from(sentence).getSortedWords();
				
				if(sortedWords.size() > maxWords.get()){
					maxWords.set(sortedWords.size());
				}
								
				Optional<String> csvString = sortedWords.stream().reduce((result, element) -> result + "," + element);					
				writer.printf("Sentence %d, %s%n", sentenceIndex.incrementAndGet(), csvString.get());										
			});
		};
		
		return maxWords.get();
	}
	
	private String generateHeader(int maxWordCount){
		String header = "";
		for(int i=1; i<=maxWordCount;++i){
			header+=", Word "+i;
		}
		return header+"\n";
	}
	
	private File createTmpFile() throws IOException{
		File tempDir = Files.createTempDir();
		File tempFile = File.createTempFile("tmp", "txt", tempDir);
		tempFile.deleteOnExit();
		
		return tempFile;
	}
	
	private PrintWriter createTmpFileWriter(File tmpFile) throws IOException {
		return new PrintWriter(new BufferedWriter(new FileWriter(tmpFile)));
	}
	
	
}
