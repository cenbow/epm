package br.net.woodstock.epm.office.oo;

public class PipeOpenOfficeConnection extends AbstractOpenOfficeConnection {

	private static final String	UNO_URL	= "pipe,name=%s;urp;StarOffice.ComponentContext";

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
