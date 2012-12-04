package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.OfficeException;
import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeExecutor;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;

public class AbstractOpenOfficeManager implements OpenOfficeManager {

	private OpenOfficeConnection	connection;

	public AbstractOpenOfficeManager(final OpenOfficeConnection connection) {
		super();
		this.connection = connection;
	}

	protected void connect() {
		this.connection.connect();
	}

	protected void close() {
		this.connection.connect();
	}

	@Override
	public OpenOfficeConnection getConnection() {
		return this.connection;
	}

	protected void setConnection(final OpenOfficeConnection connection) {
		this.connection = connection;
	}

	@Override
	public <T> T execute(final OpenOfficeExecutor executor) {
		try {
			this.connect();
			T t = executor.doInConnection(this.connection);
			return t;
		} catch (Exception e) {
			OfficeLog.getLogger().error(e.getMessage(), e);
			throw new OfficeException(e);
		} finally {
			try {
				this.close();
			} catch (Exception e) {
				//
			}
		}
	}
}
