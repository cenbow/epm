package br.net.woodstock.epm.web.security;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;

public class EPMAuthenticationProvider implements AuthenticationProvider, Serializable {

	private static final long	serialVersionUID	= -7746636115602950148L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public EPMAuthenticationProvider() {
		super();
	}

	@Override
	public Authentication authenticate(final Authentication authentication) {
		String login = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		User user = this.securityService.getUserByLoginPassword(login, password);
		if (user == null) {
			throw new BadCredentialsException("User not found");
		}
		return EPMAuthenticationHelper.toAuthentication(user);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean supports(final Class clazz) {
		if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

}
