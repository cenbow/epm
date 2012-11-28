package br.net.woodstock.epm.office.oo;

import br.net.woodstock.epm.office.OfficeException;
import br.net.woodstock.epm.util.EPMLog;

public class AbstractOpenOfficeManager {

	private String	host;

	private int		port;

	public AbstractOpenOfficeManager(final String host, final int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public synchronized <T> T execute(final OpenOfficeExecutor executor) {
		OpenOfficeConnection connection = null;
		try {
			connection = new OpenOfficeConnection(this.host, this.port);
			T t = executor.doInConnection(connection.getComponentLoader());
			return t;
		} catch (Exception e) {
			EPMLog.getLogger().error(e.getMessage(), e);
			throw new OfficeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					//
				}
			}
		}
	}
}
