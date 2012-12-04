package br.net.woodstock.epm.office.oo.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sun.star.io.XInputStream;
import com.sun.star.io.XOutputStream;

public class OpenOfficeIO {

	public static XInputStream toXInputStream(final byte[] bytes) {
		return new XInputStreamImpl(bytes);
	}

	public static XInputStream toXInputStream(final InputStream inputStream) throws IOException {
		if (inputStream instanceof XInputStream) {
			return (XInputStream) inputStream;
		}
		return new XInputStreamImpl(inputStream);
	}

	public static XOutputStream toXOutputStream(final OutputStream outputStream) {
		return new XOutputStreamImpl(outputStream);
	}

}
