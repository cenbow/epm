package br.net.woodstock.epm.office.oo;

public class OpenOfficeManager extends AbstractOpenOfficeManager {

	private static final String	LOCALHOST	= "localhost";

	public OpenOfficeManager(final int port) {
		super(OpenOfficeManager.LOCALHOST, port);
	}

	public OpenOfficeManager(final String host, final int port) {
		super(host, port);
	}

}
