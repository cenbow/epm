package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

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
		return list.toArray(new String[list.size()]);
	}

	public String getName() {
		return this.name;
	}

}
