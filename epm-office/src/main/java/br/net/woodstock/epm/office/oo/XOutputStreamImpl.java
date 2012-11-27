package br.net.woodstock.epm.office.oo;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.star.io.XOutputStream;

class XOutputStreamImpl implements XOutputStream {

	private OutputStream	outputStream;

	public XOutputStreamImpl(final OutputStream outputStream) {
		super();
		this.outputStream = outputStream;
	}

	@Override
	public void writeBytes(final byte[] bytes) throws com.sun.star.io.IOException {
		try {
			this.outputStream.write(bytes);
		} catch (IOException e) {
			throw (new com.sun.star.io.IOException(e.getMessage()));
		}
	}

	@Override
	public void closeOutput() throws com.sun.star.io.IOException {
		try {
			this.outputStream.flush();
			this.outputStream.close();
		} catch (java.io.IOException e) {
			throw (new com.sun.star.io.IOException(e.getMessage()));
		}
	}

	@Override
	public void flush() throws com.sun.star.io.IOException {
		try {
			this.outputStream.flush();
		} catch (IOException e) {
			throw (new com.sun.star.io.IOException(e.getMessage()));
		}
	}

}
