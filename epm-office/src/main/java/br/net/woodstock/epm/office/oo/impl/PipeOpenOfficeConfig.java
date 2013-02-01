package br.net.woodstock.epm.office.oo.impl;

public class PipeOpenOfficeConfig extends AbstractOpenOfficeConfig {

	private String	name;

	public PipeOpenOfficeConfig(final String name) {
		super(PipeOpenOfficeHelper.getConnectionUrl(name), PipeOpenOfficeHelper.getAcceptUrl(name));
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
