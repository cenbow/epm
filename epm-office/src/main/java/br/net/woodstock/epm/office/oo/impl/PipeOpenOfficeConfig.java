package br.net.woodstock.epm.office.oo.impl;

public class PipeOpenOfficeConfig extends AbstractOpenOfficeConfig {

	private static final String	CONNECTION_URL	= "pipe,name=%s;urp;StarOffice.ServiceManager";

	private static final String	ACCEPT			= "pipe,name=%s;urp;StarOffice.ServiceManager";

	private String				name;

	public PipeOpenOfficeConfig(final String name) {
		super(String.format(PipeOpenOfficeConfig.CONNECTION_URL, name), String.format(PipeOpenOfficeConfig.ACCEPT, name));
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
