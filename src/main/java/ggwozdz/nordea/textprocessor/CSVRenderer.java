package ggwozdz.nordea.textprocessor;

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
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class CSVRenderer {
	private static final Logger LOG = LoggerFactory.getLogger(CSVRenderer.class);
	
	private final InputStreamSplitter splitter;
	
	@Inject
	public CSVRenderer(InputStreamSplitter splitter) {
		this.splitter = splitter;
	}
	
	public void renderToOutputFrom(InputStream is, OutputStream os) throws IOException {
		// need to write to tmp file first not to loose data
		// then scan through all sentences and determine the one with max no of words to generate header
		// finally generate header and copy file to output
		File tmpFile = this.createTmpFile();
		int maxWords = this.writeToTempAndCountMaxWords(is, tmpFile);
		String header = this.generateHeader(maxWords);
		
		ByteStreams.copy(new ByteArrayInputStream(header.getBytes()), os);
		ByteStreams.copy(new FileInputStream(tmpFile), os);
		
	}
	
	private int writeToTempAndCountMaxWords(InputStream is, File tmpFile) throws IOException{
		
		AtomicInteger maxWords = new AtomicInteger();
		AtomicInteger sentenceIndex = new AtomicInteger();
		
		try(PrintWriter writer = createTmpFileWriter(tmpFile)){
			this.splitter.split(is, sentence -> {
				sentenceIndex.getAndIncrement();
				
				if(sentenceIndex.get() % 1000 == 0){
					LOG.trace("Processed {} sentences", sentenceIndex);
				}
				
				List<String> sortedWords = new WordList(sentence).getSortedWords();
				
				if(sortedWords.size() > maxWords.get()){
					maxWords.set(sortedWords.size());
				}
				
				String csvString = sortedWords.stream()
					.reduce("Sentence "+sentenceIndex.get(), (reduced, currWord) -> reduced + "," + currWord);
				writer.println(csvString);										
			});
		};
		
		return maxWords.get();
	}
	
	private String generateHeader(int maxWordCount){
		String header = "";
		for(int i=1; i<=maxWordCount;++i){
			header+=", Word "+i;
		}
		return header;
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
