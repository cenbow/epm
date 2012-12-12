package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.oo.OpenOfficeConnection;

public class PoolableOpenOfficeConnection implements OpenOfficeConnection {

	private OpenOfficeConnection	wrapper;

	private boolean					connected;

	public PoolableOpenOfficeConnection(final OpenOfficeConnection wrapper) {
		super();
		this.wrapper = wrapper;
		this.connected = false;
	}

	@Override
	public boolean isConnected() {
		return this.connected;
	}

	@Override
	public void connect() {
		if (!this.wrapper.isConnected()) {
			this.wrapper.connect();
		}
		this.connected = true;
	}

	@Override
	public void close() {
		// this.wrapper.close();
		this.connected = false;
	}

	@Override
	public Object getDelegate() {
		return this.wrapper.getDelegate();
	}

	public OpenOfficeConnection getWrapper() {
		return this.wrapper;
	}

}
