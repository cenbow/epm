package br.net.woodstock.epm.web.security.departmentSkell;

import br.net.woodstock.epm.web.AbstractSearch;

public class DepartmentSkellSearch extends AbstractSearch {

	private static final long	serialVersionUID	= 641611185127306246L;

	private String				name;

	public DepartmentSkellSearch() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
