package br.net.woodstock.epm.web.security;

import br.net.woodstock.epm.web.AbstractForm;

public class LogonForm extends AbstractForm {

	private static final long	serialVersionUID	= 2089949524511461866L;

	private String				login;

	private String				password;

	public LogonForm() {
		super();
	}

	@Override
	public void reset() {
		this.setLogin(null);
		this.setPassword(null);
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
