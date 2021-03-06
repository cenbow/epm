package br.net.woodstock.epm.client.process;

import java.io.Serializable;

public class ProcessDefinition implements Serializable {

	private static final long	serialVersionUID	= -1763721657091463360L;

	private String				id;

	private String				key;

	private String				name;

	private String				version;

	public ProcessDefinition() {
		super();
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(final String key) {
		this.key = key;
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
