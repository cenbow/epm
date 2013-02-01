package br.net.woodstock.epm.office.oo.impl;

abstract class SocketOpenOfficeHelper {

	public static final String	ACCEPT_URL		= "socket,host=%s,port=%d;urp;StarOffice.ServiceManager";

	public static final String	CONNECTION_URL	= "socket,host=%s,port=%d,tcpNoDelay=1";

	public static final String	DEFAULT_HOST	= "127.0.0.1";

	private SocketOpenOfficeHelper() {
		//
	}

	public static String getAcceptUrl(final int port) {
		return String.format(SocketOpenOfficeHelper.ACCEPT_URL, SocketOpenOfficeHelper.DEFAULT_HOST, Integer.valueOf(port));
	}

	public static String getAcceptUrl(final String host, final int port) {
		return String.format(SocketOpenOfficeHelper.ACCEPT_URL, host, Integer.valueOf(port));
	}

	public static String getConnectionUrl(final int port) {
		return String.format(SocketOpenOfficeHelper.CONNECTION_URL, SocketOpenOfficeHelper.DEFAULT_HOST, Integer.valueOf(port));
	}

	public static String getConnectionUrl(final String host, final int port) {
		return String.format(SocketOpenOfficeHelper.CONNECTION_URL, host, Integer.valueOf(port));
	}

}
