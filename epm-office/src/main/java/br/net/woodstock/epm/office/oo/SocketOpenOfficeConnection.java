package br.net.woodstock.epm.office.oo;

public class SocketOpenOfficeConnection extends AbstractOpenOfficeConnection {

	private static final String	UNO_URL	= "socket,host=%s,port=%d,tcpNoDelay=1";

	private String				url;

	public SocketOpenOfficeConnection(final String host, final int port) {
		super();
		this.url = String.format(SocketOpenOfficeConnection.UNO_URL, host, Integer.valueOf(port));
	}

	@Override
	public String getConnectionURL() {
		return this.url;
	}
}
