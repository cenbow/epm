package br.net.woodstock.epm.web.security.user;

import br.net.woodstock.epm.web.AbstractSearch;

public class UserRoleSearch extends AbstractSearch {

	private static final long	serialVersionUID	= 975577930458537052L;

	private Integer				userId;

	public UserRoleSearch() {
		super();
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(final Integer userId) {
		this.userId = userId;
	}

}
