package br.net.woodstock.epm.office.test;

import br.net.woodstock.epm.office.oo.impl.AbstractOpenOfficeServer;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficeServer;

public final class SocketOpenOfficeServerTestMain {

	public SocketOpenOfficeServerTestMain() {
		super();
	}

	public static void main(final String[] args) {
		AbstractOpenOfficeServer server = null;
		try {
			server = new SocketOpenOfficeServer(8100);
			server.start();

			/*
			 * Socket socket = new Socket("localhost", 8100); OutputStream outputStream =
			 * socket.getOutputStream(); outputStream.write("Teste".getBytes()); socket.shutdownOutput();
			 * outputStream.close(); socket.close();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				server.stop();
			}
		}
	}
}
