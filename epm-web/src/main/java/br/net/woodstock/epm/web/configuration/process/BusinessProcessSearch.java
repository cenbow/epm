package br.net.woodstock.epm.web.configuration.process;

import br.net.woodstock.epm.web.AbstractSearch;

public class BusinessProcessSearch extends AbstractSearch {

	private static final long	serialVersionUID	= 4341255763876717170L;

	private String				name;

	public BusinessProcessSearch() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
