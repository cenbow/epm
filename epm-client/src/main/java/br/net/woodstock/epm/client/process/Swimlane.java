package br.net.woodstock.epm.client.process;

import java.io.Serializable;

public class Swimlane implements Serializable {

	private static final long	serialVersionUID	= 3714899291347828546L;

	private String				id;

	private String				name;

	public Swimlane() {
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
