package br.net.woodstock.epm.web.configuration.process;

import br.net.woodstock.epm.web.AbstractSearch;

public class ProcessSearch extends AbstractSearch {

	private static final long	serialVersionUID	= 4341255763876717170L;

	private String				name;

	public ProcessSearch() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
