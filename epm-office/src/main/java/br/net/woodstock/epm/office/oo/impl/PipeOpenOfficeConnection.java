package br.net.woodstock.epm.office.oo.impl;

public class PipeOpenOfficeConnection extends AbstractOpenOfficeConnection {

	private static final String	UNO_URL	= "pipe,name=%s;urp;StarOffice.ServiceManager";

	private String				url;

	public PipeOpenOfficeConnection(final String name) {
		super();
		this.url = String.format(PipeOpenOfficeConnection.UNO_URL, name);
	}

	@Override
	public String getConnectionURL() {
		return this.url;
	}
}
