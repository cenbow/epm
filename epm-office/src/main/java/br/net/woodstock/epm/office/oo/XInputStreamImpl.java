package br.net.woodstock.epm.office.oo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import br.net.woodstock.rockframework.utils.IOUtils;

import com.sun.star.io.BufferSizeExceededException;
import com.sun.star.io.NotConnectedException;
import com.sun.star.io.XInputStream;
import com.sun.star.io.XSeekable;
import com.sun.star.lang.IllegalArgumentException;

class XInputStreamImpl extends ByteArrayInputStream implements XInputStream, XSeekable {

	public XInputStreamImpl(final InputStream inputStream) throws IOException {
		super(IOUtils.toByteArray(inputStream));
	}

	public XInputStreamImpl(final byte[] buf) {
		super(buf);
	}

	//
	// Implement XInputStream
	//

	@Override
	public int readBytes(final byte[][] buffer, final int bufferSize) throws NotConnectedException, BufferSizeExceededException, com.sun.star.io.IOException {
		int numberOfReadBytes;
		try {
			byte[] bytes = new byte[bufferSize];
			numberOfReadBytes = super.read(bytes);
			if (numberOfReadBytes > 0) {
				if (numberOfReadBytes < bufferSize) {
					byte[] smallerBuffer = new byte[numberOfReadBytes];
					System.arraycopy(bytes, 0, smallerBuffer, 0, numberOfReadBytes);
					bytes = smallerBuffer;
				}
			} else {
				bytes = new byte[0];
				numberOfReadBytes = 0;
			}

			buffer[0] = bytes;
			return numberOfReadBytes;
		} catch (java.io.IOException e) {
			throw new com.sun.star.io.IOException(e.getMessage(), this);
		}
	}

	@Override
	public int readSomeBytes(final byte[][] buffer, final int bufferSize) throws NotConnectedException, BufferSizeExceededException, com.sun.star.io.IOException {
		return this.readBytes(buffer, bufferSize);
	}

	@Override
	public void skipBytes(final int skipLength) throws NotConnectedException, BufferSizeExceededException, com.sun.star.io.IOException {
		this.skip(skipLength);
	}

	@Override
	public void closeInput() throws NotConnectedException, com.sun.star.io.IOException {
		try {
			this.close();
		} catch (java.io.IOException e) {
			throw new com.sun.star.io.IOException(e.getMessage(), this);
		}
	}

	//
	// Implement XSeekable
	//

	@Override
	public long getLength() throws com.sun.star.io.IOException {
		return this.count;
	}

	@Override
	public long getPosition() throws com.sun.star.io.IOException {
		return this.pos;
	}

	@Override
	public void seek(final long position) throws IllegalArgumentException, com.sun.star.io.IOException {
		this.pos = (int) position;
	}

}
