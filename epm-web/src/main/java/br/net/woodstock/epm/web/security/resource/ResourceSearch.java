package br.net.woodstock.epm.web.security.resource;

import br.net.woodstock.epm.web.AbstractSearch;

public class ResourceSearch extends AbstractSearch {

	private static final long	serialVersionUID	= 1311239501143714257L;

	private String				name;

	public ResourceSearch() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
