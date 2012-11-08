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
import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.repository.util.RepositoryHelper;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.rockframework.domain.service.ServiceException;
import br.net.woodstock.rockframework.persistence.orm.GenericRepository;
import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryMetadata;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;
import br.net.woodstock.rockframework.persistence.orm.QueryableRepository;
import br.net.woodstock.rockframework.security.util.PasswordEncoder;
import br.net.woodstock.rockframework.security.util.SHA1PasswordEncoder;
import br.net.woodstock.rockframework.utils.ConditionUtils;

@Service
public class SecurityServiceImpl implements SecurityService {

	private static final long	serialVersionUID				= 7929971241857912337L;

	// USER
	private static final String	JPQL_GET_USER_BY_LOGIN_PASSWORD	= "SELECT u FROM User AS u LEFT OUTER JOIN FETCH u.roles AS r LEFT OUTER JOIN FETCH r.resources AS rr LEFT OUTER JOIN FETCH u.certificates AS c WHERE u.login = :login AND u.password = :password";

	private static final String	JPQL_LIST_USER_BY_NAME			= "SELECT u FROM User AS u WHERE lower(u.name) LIKE lower(:name) ORDER BY u.name";

	private static final String	JPQL_COUNT_USER_BY_NAME			= "SELECT COUNT(*) FROM User AS u WHERE lower(u.name) LIKE lower(:name)";

	// ROLE
	private static final String	JPQL_LIST_ROLE_BY_NAME			= "SELECT r FROM Role AS r WHERE lower(r.name) LIKE lower(:name) ORDER BY r.name";

	private static final String	JPQL_COUNT_ROLE_BY_NAME			= "SELECT COUNT(*) FROM Role AS r WHERE lower(r.name) LIKE lower(:name)";

	// RESOURCE
	private static final String	JPQL_LIST_RESOURCE_BY_NAME		= "SELECT r FROM Resource AS r WHERE lower(r.name) LIKE lower(:name) ORDER BY r.name";

	private static final String	JPQL_COUNT_RESOURCE_BY_NAME		= "SELECT COUNT(*) FROM Resource AS r WHERE lower(r.name) LIKE lower(:name)";

	@Autowired(required = true)
	private GenericRepository	genericRepository;

	@Autowired(required = true)
	private QueryableRepository	queryableRepository;

	private PasswordEncoder		encoder;

	public SecurityServiceImpl() {
		super();
		this.encoder = new SHA1PasswordEncoder();
	}

	// User
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public User getUserById(final Integer id) {
		try {
			return this.genericRepository.get(new User(id));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public User getUserByLoginPassword(final String login, final String password) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("login", login);
			parameters.put("password", this.encoder.encode(password));
			QueryMetadata metadata = RepositoryHelper.toQueryMetadata(SecurityServiceImpl.JPQL_GET_USER_BY_LOGIN_PASSWORD, parameters);
			User user = this.queryableRepository.getSingle(metadata);
			return user;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveUser(final User user) {
		try {
			user.setActive(Boolean.TRUE);
			user.setPassword(this.encoder.encode(user.getPassword()));
			this.genericRepository.save(user);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateUser(final User user) {
		try {
			User u = this.genericRepository.get(user);
			u.setActive(user.getActive());
			u.setEmail(user.getEmail());
			u.setLogin(user.getLogin());
			u.setName(user.getName());

			if (ConditionUtils.isNotEmpty(user.getPassword())) {
				user.setPassword(this.encoder.encode(user.getPassword()));
			}

			this.genericRepository.update(u);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public QueryResult listUsersByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", RepositoryHelper.getLikeValue(name, false));

			QueryMetadata metadata = RepositoryHelper.toQueryMetadata(SecurityServiceImpl.JPQL_LIST_USER_BY_NAME, SecurityServiceImpl.JPQL_COUNT_USER_BY_NAME, page, parameters);
			return this.queryableRepository.getCollection(metadata);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveUserRoles(final User user, final Role... roles) {
		try {
			User u = this.genericRepository.get(user);
			if (u != null) {
				if (u.getRoles() == null) {
					u.setRoles(new HashSet<Role>());
				}

				Iterator<Role> it = u.getRoles().iterator();
				while (it.hasNext()) {
					Role r = it.next();
					boolean delete = true;
					for (Role tmp : roles) {
						if (r.getId().equals(tmp.getId())) {
							delete = false;
							break;
						}
					}
					if (delete) {
						it.remove();
					}
				}
				for (Role r : roles) {
					Role tmp = this.genericRepository.get(r);
					if (tmp != null) {
						u.getRoles().add(tmp);
					}
				}
				this.genericRepository.update(u);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Role
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Role getRoleById(final Integer id) {
		try {
			return this.genericRepository.get(new Role(id));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveRole(final Role role) {
		try {
			role.setActive(Boolean.TRUE);
			this.genericRepository.save(role);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateRole(final Role role) {
		try {
			Role r = this.genericRepository.get(role);
			r.setActive(role.getActive());
			r.setName(role.getName());
			this.genericRepository.update(r);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public QueryResult listRolesByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", RepositoryHelper.getLikeValue(name, false));

			QueryMetadata metadata = RepositoryHelper.toQueryMetadata(SecurityServiceImpl.JPQL_LIST_ROLE_BY_NAME, SecurityServiceImpl.JPQL_COUNT_ROLE_BY_NAME, page, parameters);
			return this.queryableRepository.getCollection(metadata);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Resource
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Resource getResourceById(final Integer id) {
		try {
			return this.genericRepository.get(new Resource(id));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveResource(final Resource resource) {
		try {
			resource.setActive(Boolean.TRUE);
			this.genericRepository.save(resource);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateResource(final Resource resource) {
		try {
			Resource r = this.genericRepository.get(resource);
			r.setActive(resource.getActive());
			r.setName(resource.getName());
			this.genericRepository.update(r);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public QueryResult listResourcesByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", RepositoryHelper.getLikeValue(name, false));

			QueryMetadata metadata = RepositoryHelper.toQueryMetadata(SecurityServiceImpl.JPQL_LIST_RESOURCE_BY_NAME, SecurityServiceImpl.JPQL_COUNT_RESOURCE_BY_NAME, page, parameters);
			return this.queryableRepository.getCollection(metadata);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// Certificate
	@Override
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Certificate getCertificateById(final Integer id) {
		try {
			return this.genericRepository.get(new Certificate(id));
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveCertificate(final Certificate certificate) {
		try {
			certificate.setActive(Boolean.TRUE);
			this.genericRepository.save(certificate);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
