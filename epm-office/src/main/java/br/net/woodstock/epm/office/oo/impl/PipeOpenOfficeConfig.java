package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.oo.OpenOfficeConfig;
import br.net.woodstock.rockframework.utils.CollectionUtils;

public class PipeOpenOfficeConfig implements OpenOfficeConfig {

	private String[]	startupCommand;

	private String		connectionUrl;

	private String		name;

	public PipeOpenOfficeConfig(final String name) {
		super();
		this.name = name;

		this.connectionUrl = String.format("pipe,name=%s;urp;StarOffice.ServiceManager", name);

		List<String> list = new ArrayList<String>();
		list.add("soffice");
		list.add("--accept=\"pipe,name=" + this.name + ";urp;\"");
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

	@Override
	public String getConnectionUrl() {
		return this.connectionUrl;
	}

	public String getName() {
		return this.name;
	}

}
