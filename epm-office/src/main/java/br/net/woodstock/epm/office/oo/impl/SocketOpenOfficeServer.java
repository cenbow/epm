package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.rockframework.utils.CollectionUtils;

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
		list.add("--accept=\"socket,host=127.0.0.1,port=" + this.port + ";urp;StarOffice.ServiceManager\"");
		list.add("--nofirststartwizard");
		list.add("--headless");
		list.add("--nocrashreport");
		list.add("--nodefault");
		list.add("--nolockcheck");
		list.add("--nologo");
		list.add("--norestore");
		return CollectionUtils.toArray(list, String.class);
	}

	@Override
	public OpenOfficeConnection getConnection() {
		return new SocketOpenOfficeConnection("127.0.0.1", this.getPort());
	}

	public int getPort() {
		return this.port;
	}

}
