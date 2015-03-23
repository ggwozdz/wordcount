package ggwozdz.nordea.textprocessor;

import ggwozdz.nordea.textsplitter.InputStreamSplitterModule;

import com.google.inject.AbstractModule;

public class TextProcessorModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new InputStreamSplitterModule());
		bind(XMLRenderer.class);
		bind(CSVRenderer.class);
		bind(TextProcessor.class).to(NordeaTextProcessor.class);
		
	}

}
