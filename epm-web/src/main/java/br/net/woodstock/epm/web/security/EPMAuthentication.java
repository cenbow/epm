package br.net.woodstock.epm.web.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;

public class EPMAuthentication implements Authentication {

	private static final long				serialVersionUID	= 6797136534797989459L;

	private Integer							id;

	private String							name;

	private String							description;

	private Collection<EPMGrantedAuthority>	grants;

	public EPMAuthentication(final Integer id, final String name, final String description, final Collection<EPMGrantedAuthority> grants) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.grants = grants;
	}

	public Integer getId() {
		return this.id;
	}

	@Override
	public Collection<EPMGrantedAuthority> getAuthorities() {
		return this.grants;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return this.description;
	}

	@Override
	public Object getPrincipal() {
		return this.name;
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
		return this.name;
	}

}
