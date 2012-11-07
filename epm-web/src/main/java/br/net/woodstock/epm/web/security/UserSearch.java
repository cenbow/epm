package br.net.woodstock.epm.web.security;

import java.io.Serializable;

public class UserSearch implements Serializable {

	private static final long	serialVersionUID	= 3794876725206200806L;

	private String				name;

	public UserSearch() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
