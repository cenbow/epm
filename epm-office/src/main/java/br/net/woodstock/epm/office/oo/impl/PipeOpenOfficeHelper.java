package br.net.woodstock.epm.office.oo.impl;

abstract class PipeOpenOfficeHelper {

	private static final String	CONNECTION_URL	= "pipe,name=%s;urp;StarOffice.ServiceManager";

	private static final String	ACCEPT_URL		= "pipe,name=%s;urp;StarOffice.ServiceManager";

	private PipeOpenOfficeHelper() {
		//
	}

	public static String getAcceptUrl(final String name) {
		return String.format(PipeOpenOfficeHelper.ACCEPT_URL, name);
	}

	public static String getConnectionUrl(final String name) {
		return String.format(PipeOpenOfficeHelper.CONNECTION_URL, name);
	}

}
