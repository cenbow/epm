package br.net.woodstock.epm.web.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;

public class LocalAuthentication implements Authentication {

	private static final long				serialVersionUID	= -3906765045258024257L;

	private static final String				ROLE_PREFIX			= "ROLE_";

	private User							user;

	private Collection<GrantedAuthority>	grants;

	public LocalAuthentication(final User user) {
		this.user = user;

		this.grants = new ArrayList<GrantedAuthority>();
		for (Role role : user.getRoles()) {
			this.grants.add(new SimpleGrantedAuthority(LocalAuthentication.ROLE_PREFIX + role.getName()));
		}
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return this.grants;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return this.user.getName();
	}

	@Override
	public Object getPrincipal() {
		return this.user.getLogin();
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public void setAuthenticated(final boolean flag) {
		//
	}

	@Override
	public String getName() {
		return this.user.getName();
	}

}
