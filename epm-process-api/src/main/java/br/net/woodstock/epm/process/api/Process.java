package br.net.woodstock.epm.process.api;

import java.io.Serializable;

public class Process implements Serializable {

	private static final long	serialVersionUID	= -1763721657091463360L;

	private String				id;

	private String				name;

	private String				version;

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

	public String getVersion() {
		return this.version;
	}

	public void setVersion(final String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return this.getId();
	}

}
