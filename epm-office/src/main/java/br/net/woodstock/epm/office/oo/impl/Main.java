package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.oo.OpenOfficeConfig;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;

public final class Main implements Runnable {

	private int	port;

	private Main(final int port) {
		super();
		this.port = port;
	}

	@Override
	public void run() {
		System.out.println("Create server on port " + this.port);
		System.out.println("Starting...");
		OpenOfficeConfig config = new SocketOpenOfficeConfig(this.port);
		OpenOfficeManager manager = new SynchronizedOpenOfficeManager(config);
		System.out.println("Server started " + manager);
	}

	public static void main(final String[] args) {
		Thread thread = new Thread(new Main(Integer.parseInt(args[0])));
		thread.start();
	}

}
