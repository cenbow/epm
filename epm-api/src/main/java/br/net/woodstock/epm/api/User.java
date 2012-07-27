package br.net.woodstock.epm.api;

import br.net.woodstock.rockframework.domain.Pojo;

public class User implements Pojo {

	private static final long	serialVersionUID	= -1932408409262519409L;

	private String				id;

	private String				login;

	private String				name;

	private String				email;

	private String				password;

	private UserStatus			status;

	private Role				role;

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

	public UserStatus getStatus() {
		return this.status;
	}

	public void setStatus(final UserStatus status) {
		this.status = status;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(final Role role) {
		this.role = role;
	}

}
