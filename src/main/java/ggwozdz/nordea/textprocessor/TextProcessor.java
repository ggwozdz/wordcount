package ggwozdz.nordea.textprocessor;

import ggwozdz.nordea.OutputFormat;

import java.io.IOException;
import java.io.InputStream;

public interface TextProcessor {
	void processText(InputStream is, OutputFormat outputFormat) throws IOException;
}
