package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeException;
import br.net.woodstock.epm.office.oo.OpenOfficeExecutor;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;

public class AbstractOpenOfficeManager implements OpenOfficeManager {

	private OpenOfficeConnection	connection;

	public AbstractOpenOfficeManager(final OpenOfficeConnection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public OpenOfficeConnection getConnection() {
		return this.connection;
	}

	@Override
	public <T> T execute(final OpenOfficeExecutor executor) {
		try {
			OfficeLog.getLogger().info("Executing " + executor.getClass().getName());

			long l = System.currentTimeMillis();

			T t = executor.doInConnection(this.getConnection());

			l = System.currentTimeMillis() - l;
			OfficeLog.getLogger().info("Executed in " + l + "ms");

			return t;
		} catch (Exception e) {
			OfficeLog.getLogger().error(e.getMessage(), e);
			throw new OpenOfficeException(e);
		}
	}
}
