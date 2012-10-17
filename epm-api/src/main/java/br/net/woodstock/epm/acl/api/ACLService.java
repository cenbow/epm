package br.net.woodstock.epm.acl.api;

import java.io.Serializable;

public interface ACLService extends Serializable {

	// User
	User getUserByLoginPassword(String login, String password);

	void saveUser(User user);

	void updateUser(User user);

	// Role
	Role getRoleById(String id);

	Role[] listRole();

	void saveRole(Role role);

	void updateRole(Role role);

	// Resource
	Resource getResourceById(String id);

	Resource[] listResource();

	void saveResource(Resource resource);

	void updateResource(Resource resource);

	// Certificate
	Certificate getCertificateById(String id);

	Certificate[] listCertificateByUser(String userId);

	void saveCertificate(Certificate certificate);

	void updateCertificate(Certificate certificate);
}
