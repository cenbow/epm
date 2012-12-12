package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeServer;

public class PoolableOpenOfficeServer implements OpenOfficeServer {

	private OpenOfficeServer	wrapper;

	public PoolableOpenOfficeServer(final OpenOfficeServer wrapper) {
		super();
		this.wrapper = wrapper;
	}

	@Override
	public void start() {
		this.wrapper.start();
	}

	@Override
	public void stop() {
		this.wrapper.stop();
	}

	@Override
	public boolean isRunning() {
		return this.wrapper.isRunning();
	}

	@Override
	public OpenOfficeConnection getConnection() {
		OpenOfficeConnection ooc = this.wrapper.getConnection();
		PoolableOpenOfficeConnection pooc = new PoolableOpenOfficeConnection(ooc);
		return pooc;
	}

}
