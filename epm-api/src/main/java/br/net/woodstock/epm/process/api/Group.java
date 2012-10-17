package br.net.woodstock.epm.process.api;

import java.io.Serializable;

public class Group implements Serializable {

	private static final long	serialVersionUID	= 7491145120355168286L;

	private String				id;

	private String				name;

	public Group() {
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
