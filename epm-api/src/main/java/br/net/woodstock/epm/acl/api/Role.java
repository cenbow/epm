package br.net.woodstock.epm.acl.api;

import java.io.Serializable;
import java.util.Set;

public class Role implements Serializable {

	private static final long	serialVersionUID	= -1932408409262519409L;

	private String				id;

	private String				name;

	private Boolean				active;

	private Set<Resource>		resources;

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

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Set<Resource> getResources() {
		return this.resources;
	}

	public void setResources(final Set<Resource> resources) {
		this.resources = resources;
	}

}
