package br.net.woodstock.epm.office.oo.impl;

public class Main {

	public Main() {
		super();
	}

	public static void main(String[] args) {
		AbstractOpenOfficeServer server = null;
		try {
			int port = Integer.parseInt(args[0]);
			System.out.println("Create server on port " + port);
			server = new SocketOpenOfficeServer(port);
			System.out.println("Starting...");
			server.start();
			System.out.println("Server started");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				server.stop();
			}
		}
	}

}
