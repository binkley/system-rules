package org.junit.contrib.java.lang.system;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * {@code LogModeBehavior} encapsulates the behavior of log modes for {@link
 * org.junit.contrib.java.lang.system.internal.PrintStreamLog}.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 */
public interface LogModeBehavior extends AutoCloseable {
	/** All string encoding is UTF-8. */
	static final String ENCODING = "UTF-8";

	/** Creates a new stream from the original stream and the log. */
	OutputStream newStream(PrintStream originalStream, ByteArrayOutputStream log);

	/** Handles test failure. */
	void failed(PrintStream originalStream, ByteArrayOutputStream log);

	/** Closes any resources associated with this mode. */
	@Override
	void close();
}
