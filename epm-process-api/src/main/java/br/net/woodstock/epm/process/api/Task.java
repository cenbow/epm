package br.net.woodstock.epm.process.api;

import java.io.Serializable;

import br.net.woodstock.epm.api.User;

public class Task implements Serializable {

	private static final long	serialVersionUID	= -2068654141082732847L;

	private String				id;

	private String				name;

	private String				description;

	private String				status;

	private User				user;

	private User				owner;

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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public User getOwner() {
		return this.owner;
	}

	public void setOwner(final User owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return this.getId();
	}

}
