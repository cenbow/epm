package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.rockframework.utils.CollectionUtils;

public class PipeOpenOfficeServer extends AbstractOpenOfficeServer {

	private String	name;

	public PipeOpenOfficeServer(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String[] getCommand() {
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
		return CollectionUtils.toArray(list, String.class);
	}

	@Override
	public OpenOfficeConnection newConnection() {
		return new PipeOpenOfficeConnection(this.getName());
	}

	public String getName() {
		return this.name;
	}

}
