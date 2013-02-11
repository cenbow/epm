package br.net.woodstock.epm.web.security.role;

import br.net.woodstock.epm.web.AbstractSearch;

public class RoleSearch extends AbstractSearch {

	private static final long	serialVersionUID	= -848094184623390284L;

	private String				name;

	public RoleSearch() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
