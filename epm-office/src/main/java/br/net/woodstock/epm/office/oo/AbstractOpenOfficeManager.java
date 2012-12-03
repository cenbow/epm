package br.net.woodstock.epm.office.oo;

import br.net.woodstock.epm.office.OfficeException;
import br.net.woodstock.epm.util.EPMLog;

public class AbstractOpenOfficeManager {

	private AbstractOpenOfficeConnection	connection;

	public AbstractOpenOfficeManager(final AbstractOpenOfficeConnection connection) {
		super();
		this.connection = connection;
	}

	public synchronized <T> T execute(final OpenOfficeExecutor executor) {
		try {
			this.connection.connect();
			T t = executor.doInConnection(this.connection.getComponentLoader());
			return t;
		} catch (Exception e) {
			EPMLog.getLogger().error(e.getMessage(), e);
			throw new OfficeException(e);
		} finally {
			try {
				this.connection.close();
			} catch (Exception e) {
				//
			}
		}
	}
}
