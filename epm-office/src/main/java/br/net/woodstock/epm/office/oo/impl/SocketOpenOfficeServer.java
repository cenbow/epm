package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

public class SocketOpenOfficeServer extends AbstractOpenOfficeServer {

	private int	port;

	public SocketOpenOfficeServer(final int port) {
		super();
		this.port = port;
	}

	@Override
	public String[] getCommand() {
		List<String> list = new ArrayList<String>();
		list.add("soffice");
		list.add("--accept=\"socket,host=127.0.0.1,port=" + this.port + ";urp;\"");
		list.add("--nofirststartwizard");
		list.add("--headless");
		list.add("--nocrashreport");
		list.add("--nodefault");
		list.add("--nolockcheck");
		list.add("--nologo");
		list.add("--norestore");
		return list.toArray(new String[list.size()]);
	}

}
