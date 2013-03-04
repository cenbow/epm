package br.net.woodstock.epm.web.security.department;

import br.net.woodstock.epm.web.AbstractSearch;

public class DepartmentSearch extends AbstractSearch {

	private static final long	serialVersionUID	= 5368241315827349914L;

	private String				name;

	public DepartmentSearch() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
