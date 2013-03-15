package br.net.woodstock.epm.security.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.epm.orm.Certificate;
import br.net.woodstock.epm.orm.Department;
import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.orm.UserRole;
import br.net.woodstock.epm.repository.util.ORMRepositoryHelper;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.epm.security.util.PasswordHelper;
import br.net.woodstock.rockframework.core.utils.Conditions;
import br.net.woodstock.rockframework.domain.ServiceException;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMFilter;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMRepository;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class SecurityServiceImpl implements SecurityService {

	private static final long	serialVersionUID				= 7929971241857912337L;

	// USER
	private static final String	JPQL_GET_USER_BY_LOGIN_PASSWORD	= "SELECT u FROM User AS u WHERE u.login = :login AND u.password = :password";

	private static final String	JPQL_LIST_USER_BY_NAME			= "SELECT u FROM User AS u WHERE u.name LIKE :name ORDER BY u.name";

	private static final String	JPQL_COUNT_USER_BY_NAME			= "SELECT COUNT(*) FROM User AS u WHERE u.name LIKE :name";

	// ROLE
	private static final String	JPQL_LIST_ROLE_BY_NAME			= "SELECT r FROM Role AS r WHERE r.name LIKE :name ORDER BY r.name";

	private static final String	JPQL_COUNT_ROLE_BY_NAME			= "SELECT COUNT(*) FROM Role AS r WHERE r.name LIKE :name";

	// USERROLE
	private static final String	JPQL_LIST_USERROLE_BY_USER		= "SELECT ur FROM UserRole AS ur JOIN ur.user AS u JOIN ur.role AS r WHERE u.id = :id ORDER BY r.name";

	private static final String	JPQL_COUNT_USERROLE_BY_USER		= "SELECT COUNT(*) FROM UserRole AS ur JOIN ur.user AS u JOIN ur.role AS r WHERE u.id = :id";

	// RESOURCE
	private static final String	JPQL_LIST_RESOURCE_BY_NAME		= "SELECT r FROM Resource AS r WHERE r.name LIKE :name ORDER BY r.name";

	private static final String	JPQL_COUNT_RESOURCE_BY_NAME		= "SELECT COUNT(*) FROM Resource AS r WHERE r.name LIKE :name";

	@Autowired(required = true)
	private ORMRepository		repository;

	public SecurityServiceImpl() {
		super();
	}

	// User
	@Override
	public User getUserById(final Integer id) {
		try {
			return this.repository.get(User.class, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public User getUserByLoginPassword(final String login, final String password) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("login", login);
			parameters.put("password", PasswordHelper.encode(password));
			ORMFilter filter = ORMRepositoryHelper.toORMFilter(SecurityServiceImpl.JPQL_GET_USER_BY_LOGIN_PASSWORD, parameters);
			User user = this.repository.getSingle(filter);
			return user;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveUser(final User user) {
		try {
			user.setActive(Boolean.TRUE);
			user.setPassword(PasswordHelper.encode(user.getPassword()));
			this.repository.save(user);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateUser(final User user) {
		try {
			User u = this.repository.get(User.class, user.getId());
			u.setActive(user.getActive());
			u.setEmail(user.getEmail());
			u.setLogin(user.getLogin());
			u.setName(user.getName());

			if (Conditions.isNotEmpty(user.getPassword())) {
				user.setPassword(PasswordHelper.encode(user.getPassword()));
			}

			this.repository.update(u);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ORMResult listUsersByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", ORMRepositoryHelper.getLikeValue(name, false));

			ORMFilter filter = ORMRepositoryHelper.toORMFilter(SecurityServiceImpl.JPQL_LIST_USER_BY_NAME, SecurityServiceImpl.JPQL_COUNT_USER_BY_NAME, page, parameters);
			return this.repository.getCollection(filter);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Role
	@Override
	public Role getRoleById(final Integer id) {
		try {
			return this.repository.get(Role.class, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveRole(final Role role) {
		try {
			role.setActive(Boolean.TRUE);
			this.repository.save(role);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateRole(final Role role) {
		try {
			Role r = this.repository.get(Role.class, role.getId());
			r.setActive(role.getActive());
			r.setName(role.getName());
			this.repository.update(r);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ORMResult listRolesByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", ORMRepositoryHelper.getLikeValue(name, false));

			ORMFilter filter = ORMRepositoryHelper.toORMFilter(SecurityServiceImpl.JPQL_LIST_ROLE_BY_NAME, SecurityServiceImpl.JPQL_COUNT_ROLE_BY_NAME, page, parameters);
			return this.repository.getCollection(filter);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void setRoleResources(final Role role, final Resource[] resources) {
		try {
			Role r = this.repository.get(Role.class, role.getId());
			if (r != null) {
				if (r.getResources() == null) {
					r.setResources(new HashSet<Resource>());
				}

				Iterator<Resource> it = r.getResources().iterator();
				while (it.hasNext()) {
					Resource re = it.next();
					boolean delete = true;
					for (Resource tmp : resources) {
						if (re.getId().equals(tmp.getId())) {
							delete = false;
							break;
						}
					}
					if (delete) {
						it.remove();
					}
				}
				for (Resource re : resources) {
					Resource tmp = this.repository.get(Resource.class, re.getId());
					if (tmp != null) {
						r.getResources().add(tmp);
					}
				}
				this.repository.update(r);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// UserRole
	@Override
	public void saveUserRole(final UserRole userRole) {
		try {
			userRole.setActive(Boolean.TRUE);
			userRole.setUser(this.repository.get(User.class, userRole.getUser().getId()));
			userRole.setRole(this.repository.get(Role.class, userRole.getRole().getId()));

			if (userRole.getDepartment() != null) {
				userRole.setDepartment(this.repository.get(Department.class, userRole.getDepartment().getId()));
			}

			this.repository.save(userRole);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateUserRole(final UserRole userRole) {
		try {
			UserRole r = this.repository.get(UserRole.class, userRole.getId());
			r.setActive(userRole.getActive());
			r.setUser(this.repository.get(User.class, userRole.getUser().getId()));
			r.setRole(this.repository.get(Role.class, userRole.getRole().getId()));

			if (userRole.getDepartment() != null) {
				r.setDepartment(this.repository.get(Department.class, userRole.getDepartment().getId()));
			} else {
				r.setDepartment(null);
			}

			this.repository.update(r);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ORMResult listUserRolesByUser(final Integer id, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("id", id);

			ORMFilter filter = ORMRepositoryHelper.toORMFilter(SecurityServiceImpl.JPQL_LIST_USERROLE_BY_USER, SecurityServiceImpl.JPQL_COUNT_USERROLE_BY_USER, page, parameters);
			return this.repository.getCollection(filter);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Resource
	@Override
	public Resource getResourceById(final Integer id) {
		try {
			return this.repository.get(Resource.class, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveResource(final Resource resource) {
		try {
			resource.setActive(Boolean.TRUE);
			this.repository.save(resource);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateResource(final Resource resource) {
		try {
			Resource r = this.repository.get(Resource.class, resource.getId());
			r.setActive(resource.getActive());
			r.setName(resource.getName());
			this.repository.update(r);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ORMResult listResourcesByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", ORMRepositoryHelper.getLikeValue(name, false));

			ORMFilter filter = ORMRepositoryHelper.toORMFilter(SecurityServiceImpl.JPQL_LIST_RESOURCE_BY_NAME, SecurityServiceImpl.JPQL_COUNT_RESOURCE_BY_NAME, page, parameters);
			return this.repository.getCollection(filter);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Certificate
	@Override
	public Certificate getCertificateById(final Integer id) {
		try {
			return this.repository.get(Certificate.class, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveCertificate(final Certificate certificate) {
		try {
			certificate.setActive(Boolean.TRUE);
			this.repository.save(certificate);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
