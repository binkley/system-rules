package org.junit.contrib.java.lang.system;

import org.apache.commons.io.output.TeeOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Mode of the {@link org.junit.contrib.java.lang.system.StandardErrorStreamLog} and the {@link
 * org.junit.contrib.java.lang.system.StandardOutputStreamLog}.
 */
public enum LogMode
	implements LogModeBehavior {
	/**
	 * Capture the writes to the stream. Nothing is written to the stream itself.
	 */
	LOG_ONLY {
		public OutputStream newStream(PrintStream originalStream, ByteArrayOutputStream log) {
			return log;
		}

		public void failed(PrintStream originalStream, ByteArrayOutputStream log) {
			// Do nothing
		}
	},

	/**
	 * Record the writes while they are still written to the stream.
	 */
	LOG_AND_WRITE_TO_STREAM {
		public OutputStream newStream(PrintStream originalStream, ByteArrayOutputStream log) {
			return new TeeOutputStream(originalStream, log);
		}

		public void failed(PrintStream originalStream, ByteArrayOutputStream log) {
			// Do nothing
		}
	},

	/**
	 * Capture the writes to the stream.  On test failure write to the original stream.
	 */
	LOG_AND_WRITE_TO_STREAM_ON_FAILURE_ONLY {
		public OutputStream newStream(PrintStream originalStream, ByteArrayOutputStream log) {
			return log;
		}

		public void failed(PrintStream originalStream, ByteArrayOutputStream log) {
			try {
				originalStream.print(log.toString(ENCODING));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	};

	@Override
	public final void close() {
	}
}
