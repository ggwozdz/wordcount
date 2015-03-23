package ggwozdz.nordea.textsplitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public interface InputStreamSplitter {
	void split(InputStream is, Consumer<String> streamPartConsumer) throws IOException;
}
