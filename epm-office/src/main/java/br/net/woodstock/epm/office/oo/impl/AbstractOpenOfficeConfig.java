package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.oo.OpenOfficeConfig;
import br.net.woodstock.rockframework.utils.CollectionUtils;

abstract class AbstractOpenOfficeConfig implements OpenOfficeConfig {

	private String[]	startupCommand;

	private String		connectionUrl;

	public AbstractOpenOfficeConfig(final String connectionUrl, final String accept) {
		super();
		this.connectionUrl = connectionUrl;

		List<String> list = new ArrayList<String>();
		list.add("soffice");
		list.add("--accept=\"" + accept + "\"");
		list.add("--nofirststartwizard");
		list.add("--headless");
		list.add("--nocrashreport");
		list.add("--nodefault");
		list.add("--nolockcheck");
		list.add("--nologo");
		list.add("--norestore");
		this.startupCommand = CollectionUtils.toArray(list, String.class);
	}

	@Override
	public String[] getStartupCommand() {
		return this.startupCommand;
	}

	public void setStartupCommand(final String[] startupCommand) {
		this.startupCommand = startupCommand;
	}

	@Override
	public String getConnectionUrl() {
		return this.connectionUrl;
	}

	public void setConnectionUrl(final String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

}
