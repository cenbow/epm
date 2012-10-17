package br.net.woodstock.epm.process.api;

import java.io.Serializable;

public class Actor implements Serializable {

	private static final long	serialVersionUID	= -6889536899754211035L;

	private String				id;

	private String				name;

	public Actor() {
		super();
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

	@Override
	public String toString() {
		return this.getId();
	}

}
