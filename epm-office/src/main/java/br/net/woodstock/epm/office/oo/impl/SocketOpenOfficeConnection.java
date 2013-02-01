package br.net.woodstock.epm.office.oo.impl;

public class SocketOpenOfficeConnection extends DefaultOpenOfficeConnection {

	private String	host;

	private int		port;

	public SocketOpenOfficeConnection(final int port) {
		this(SocketOpenOfficeHelper.DEFAULT_HOST, port);
	}

	public SocketOpenOfficeConnection(final String host, final int port) {
		super(SocketOpenOfficeHelper.getConnectionUrl(host, port));
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

}
