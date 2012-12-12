package br.net.woodstock.epm.office.test;

import br.net.woodstock.epm.office.oo.OpenOfficeServer;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficeServer;

public final class SocketOpenOfficeServerMain {

	private SocketOpenOfficeServerMain() {
		super();
	}

	public static void main(final String[] args) {
		OpenOfficeServer server = null;
		try {
			server = new SocketOpenOfficeServer(8100);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				server.stop();
			}
		}
	}
}
