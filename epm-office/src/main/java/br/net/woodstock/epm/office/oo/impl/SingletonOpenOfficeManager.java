package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeExecutor;

public class SingletonOpenOfficeManager extends AbstractOpenOfficeManager {

	public SingletonOpenOfficeManager(final OpenOfficeConnection connection) {
		super(connection);
	}

	@Override
	public synchronized <T> T execute(final OpenOfficeExecutor executor) {
		return super.execute(executor);
	}

	@Override
	protected void connect() {
		OpenOfficeConnection connection = this.getConnection();
		if (!connection.isConnected()) {
			connection.connect();
		}
	}

	@Override
	protected void close() {
		//
	}

}
