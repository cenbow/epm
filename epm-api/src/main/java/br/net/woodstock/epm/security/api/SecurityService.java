package br.net.woodstock.epm.security.api;

import br.net.woodstock.epm.orm.Certificate;
import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.orm.UserRole;
import br.net.woodstock.rockframework.domain.Service;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

public interface SecurityService extends Service {

	// User
	User getUserById(Integer id);

	User getUserByLoginPassword(String login, String password);

	void saveUser(User user);

	void updateUser(User user);

	ORMResult listUsersByName(String name, Page page);

	// Role
	Role getRoleById(Integer id);

	void saveRole(Role role);

	void updateRole(Role role);

	ORMResult listRolesByName(String name, Page page);

	void setRoleResources(Role role, Resource[] resources);

	ORMResult listRootRoles(Page page);

	// UserRole
	ORMResult listUserRolesByUser(Integer id, Page page);

	void saveUserRole(UserRole userRole);

	void updateUserRole(UserRole userRole);

	// Resource
	Resource getResourceById(Integer id);

	void saveResource(Resource resource);

	void updateResource(Resource resource);

	ORMResult listResourcesByName(String name, Page page);

	ORMResult listResourcesByRole(Integer id, Page page);

	// Certificate
	Certificate getCertificateById(Integer id);

	void saveCertificate(Certificate certificate);
}
