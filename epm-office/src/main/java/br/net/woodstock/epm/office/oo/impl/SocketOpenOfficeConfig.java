package br.net.woodstock.epm.office.oo.impl;

public class SocketOpenOfficeConfig extends AbstractOpenOfficeConfig {

	private static final String	CONNECTION_URL	= "socket,host=127.0.0.1,port=%d,tcpNoDelay=1;urp;StarOffice.ServiceManager";

	private static final String	ACCEPT			= "socket,host=127.0.0.1,port=%d;urp;StarOffice.ServiceManager";

	private int					port;

	public SocketOpenOfficeConfig(final int port) {
		super(String.format(SocketOpenOfficeConfig.CONNECTION_URL, Integer.valueOf(port)), String.format(SocketOpenOfficeConfig.ACCEPT, Integer.valueOf(port)));
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}

}
