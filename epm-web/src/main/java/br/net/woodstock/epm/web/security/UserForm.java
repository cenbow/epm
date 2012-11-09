package br.net.woodstock.epm.web.security;

import br.net.woodstock.epm.web.AbstractForm;

public class UserForm extends AbstractForm {

	private static final long	serialVersionUID	= -1245674591920940776L;

	private Integer				id;

	private String				login;

	private String				name;

	private String				email;

	private String				password;

	private Boolean				active;

	public UserForm() {
		super();
	}

	@Override
	public void reset() {
		this.setActive(null);
		this.setEmail(null);
		this.setId(null);
		this.setLogin(null);
		this.setName(null);
		this.setPassword(null);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
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

}
