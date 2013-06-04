package br.net.woodstock.epm.web.security.role;

import br.net.woodstock.epm.web.AbstractSearch;

public class RoleResourceSearch extends AbstractSearch {

	private static final long	serialVersionUID	= -7539471446304309748L;

	private Integer				roleId;

	public RoleResourceSearch() {
		super();
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(final Integer roleId) {
		this.roleId = roleId;
	}

}
