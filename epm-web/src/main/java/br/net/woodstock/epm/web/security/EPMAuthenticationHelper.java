package br.net.woodstock.epm.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;

import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.orm.UserRole;

public abstract class EPMAuthenticationHelper {

	public static final String				ROLE_PREFIX			= "ROLE_";

	public static final String				ROLE_ANY			= "ROLE_ANY";

	private static final Collection<String>	ROLE_ANY_RESOURCES	= Collections.emptyList();

	private EPMAuthenticationHelper() {
		//
	}

	public static Authentication toAuthentication(final User user) {
		Integer id = user.getId();
		String name = user.getLogin();
		String description = user.getName();
		Collection<EPMGrantedAuthority> grants = new ArrayList<EPMGrantedAuthority>();

		grants.add(new EPMGrantedAuthority(EPMAuthenticationHelper.ROLE_ANY, EPMAuthenticationHelper.ROLE_ANY_RESOURCES));

		for (UserRole userRole : user.getRoles()) {
			Role role = userRole.getRole();
			Collection<String> permissions = new ArrayList<String>();
			for (Resource resource : role.getResources()) {
				permissions.add(resource.getName());
			}

			EPMGrantedAuthority grant = new EPMGrantedAuthority(EPMAuthenticationHelper.ROLE_PREFIX + role.getName(), permissions);
			grants.add(grant);
		}

		EPMAuthentication authentication = new EPMAuthentication(id, name, description, grants);
		return authentication;
	}
}
