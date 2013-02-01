package br.net.woodstock.epm.office.oo.impl;

public class SocketOpenOfficeConfig extends AbstractOpenOfficeConfig {

	private int	port;

	public SocketOpenOfficeConfig(final int port) {
		super(SocketOpenOfficeHelper.getConnectionUrl(port), SocketOpenOfficeHelper.getAcceptUrl(port));
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}

}
