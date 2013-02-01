package br.net.woodstock.epm.office.oo.impl;

public class PipeOpenOfficeConnection extends DefaultOpenOfficeConnection {

	private String	name;

	public PipeOpenOfficeConnection(final String name) {
		super(PipeOpenOfficeHelper.getConnectionUrl(name));
		this.name = name;
	}
	
	
	public String getName() {
		return this.name;
	}
}
