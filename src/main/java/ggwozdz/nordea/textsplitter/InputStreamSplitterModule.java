package ggwozdz.nordea.textsplitter;

import com.google.inject.AbstractModule;

public class InputStreamSplitterModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InputStreamSplitter.class).to(StreamToSentenceSplitter.class);
		bind(OnSentenceEndSplitter.class);
	}
	
}
