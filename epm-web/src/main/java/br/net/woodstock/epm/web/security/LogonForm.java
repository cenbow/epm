package br.net.woodstock.epm.web.security;

import java.io.Serializable;

public class LogonForm implements Serializable {

	private static final long	serialVersionUID	= 2089949524511461866L;

	private String				login;

	private String				password;

	public LogonForm() {
		super();
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

}
