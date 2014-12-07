package org.junit.contrib.java.lang.system.internal;

import org.junit.contrib.java.lang.system.LogModeBehavior;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public abstract class PrintStreamLog
	extends TestWatcher {
	private static final boolean NO_AUTO_FLUSH = false;
	private static final String ENCODING = "UTF-8";
	private final ByteArrayOutputStream log = new ByteArrayOutputStream();
	private final LogModeBehavior mode;
	private PrintStream originalStream;

	protected PrintStreamLog(LogModeBehavior mode) {
		if (mode == null)
			throw new NullPointerException("The LogMode is missing.");
		this.mode = mode;
	}

	@Override
	protected void starting(Description description) {
		try {
			originalStream = getOriginalStream();
			PrintStream wrappedStream = new PrintStream(mode.newStream(originalStream, log),
				NO_AUTO_FLUSH, ENCODING);
			setStream(wrappedStream);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e); // JRE missing UTF-8
		}
	}

	@Override
	protected void failed(Throwable e, Description description) {
		mode.failed(originalStream, log);
	}

	@Override
	protected void finished(Description description) {
		setStream(originalStream);
		mode.close();
	}

	protected abstract PrintStream getOriginalStream();

	protected abstract void setStream(PrintStream wrappedLog);

	/**
	 * Clears the log. The log can be used again.
	 */
	public void clear() {
		log.reset();
	}

	/**
	 * Returns the text written to the standard error stream.
	 *
	 * @return the text written to the standard error stream.
	 */
	public String getLog() {
		try {
			return log.toString(ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
