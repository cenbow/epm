package br.net.woodstock.epm.client.security;

import java.io.Serializable;

public interface SecurityService extends Serializable {

	// User
	User getUserByLoginPassword(String login, String password);

	User getUserById(String id);

	// Role
	Role getRoleById(String id);

	Role[] listRoles();

	// Resource
	Resource getResourceById(String id);

	Resource[] listResources();

	// Certificate
	Certificate getCertificateById(String id);

	Certificate[] listCertificateByUser(String userId);

}
