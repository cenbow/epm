package br.net.woodstock.epm.acl.api;

import java.io.Serializable;

public class Resource implements Serializable {

	private static final long	serialVersionUID	= 9189241373976036057L;

	private String				id;

	private String				name;

	private Boolean				active;

	public Resource() {
		super();
	}

	public Resource(final String id) {
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

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

}
