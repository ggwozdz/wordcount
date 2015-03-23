package ggwozdz.nordea.texttransform;

import ggwozdz.nordea.textsplitter.InputStreamSplitterModule;

import com.google.inject.AbstractModule;

public class TextTranformModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new InputStreamSplitterModule());
		bind(InputToCSVTransform.class);
		bind(InputToXMLTransform.class);
		bind(TextTransformer.class);		
	}
}
