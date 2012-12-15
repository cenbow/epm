package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.oo.OpenOfficeConfig;
import br.net.woodstock.rockframework.utils.CollectionUtils;

public class SocketOpenOfficeConfig implements OpenOfficeConfig {

	private String[]	startupCommand;

	private String		connectionUrl;

	private int			port;

	public SocketOpenOfficeConfig(final int port) {
		super();
		this.port = port;

		this.connectionUrl = String.format("socket,host=127.0.0.1,port=%d,tcpNoDelay=1", Integer.valueOf(port));

		List<String> list = new ArrayList<String>();
		list.add("soffice");
		list.add("--accept=\"socket,host=127.0.0.1,port=" + this.port + ";urp;StarOffice.ServiceManager\"");
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

	public int getPort() {
		return this.port;
	}

}
