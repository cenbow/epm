package br.net.woodstock.epm.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;

public class LocalAuthenticationProvider implements AuthenticationProvider {

	@Autowired(required = true)
	private SecurityService	securityService;

	
	public LocalAuthenticationProvider() {
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
		return new LocalAuthentication(user);
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
