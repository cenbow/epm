package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeCallback;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeException;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;

public class SimpleOpenOfficeManager implements OpenOfficeManager {

	private OpenOfficeConnection	connection;

	public SimpleOpenOfficeManager(final OpenOfficeConnection connection) {
		super();
		this.connection = connection;
	}

	@Override
	public synchronized <T> T execute(final OpenOfficeCallback callback) {
		try {
			OfficeLog.getLogger().info("Executing " + callback.getClass().getName());

			long l = System.currentTimeMillis();

			T t = callback.doInConnection(this.connection);

			l = System.currentTimeMillis() - l;
			OfficeLog.getLogger().info("Executed in " + l + "ms");

			return t;
		} catch (Exception e) {
			OfficeLog.getLogger().error(e.getMessage(), e);
			throw new OpenOfficeException(e);
		}
	}

}
