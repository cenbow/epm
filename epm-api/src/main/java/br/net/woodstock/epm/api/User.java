package br.net.woodstock.epm.api;

import java.util.Set;

import br.net.woodstock.rockframework.domain.Pojo;

public class User implements Pojo {

	private static final long	serialVersionUID	= -1932408409262519409L;

	private String				id;

	private String				login;

	private String				name;

	private String				email;

	private String				password;

	private Boolean				active;

	private Set<Role>			roles;

	public User() {
		super();
	}

	public User(final String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(final Set<Role> roles) {
		this.roles = roles;
	}

}
