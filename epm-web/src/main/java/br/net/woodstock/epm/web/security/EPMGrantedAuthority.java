package br.net.woodstock.epm.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class EPMGrantedAuthority implements GrantedAuthority {

	private static final long	serialVersionUID	= -2712835265919854042L;

	private String				role;

	private Collection<String>	permissions;

	public EPMGrantedAuthority(final String role, final Collection<String> permissions) {
		super();
		this.role = role.toUpperCase();
		this.permissions = permissions;
	}

	@Override
	public String getAuthority() {
		return this.getRole();
	}

	public String getRole() {
		return this.role;
	}

	public Collection<String> getPermissions() {
		return this.permissions;
	}

}
