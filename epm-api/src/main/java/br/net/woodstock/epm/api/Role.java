package br.net.woodstock.epm.api;

import br.net.woodstock.rockframework.domain.Pojo;

public class Role implements Pojo {

	private static final long	serialVersionUID	= -1932408409262519409L;

	private String				id;

	private String				name;

	private RoleStatus			status;

	public Role() {
		super();
	}

	public Role(final String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public RoleStatus getStatus() {
		return this.status;
	}

	public void setStatus(final RoleStatus status) {
		this.status = status;
	}

}
