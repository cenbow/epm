package br.net.woodstock.epm.security.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.net.woodstock.epm.orm.Department;
import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.epm.repository.util.ORMRepositoryHelper;
import br.net.woodstock.epm.security.api.LocaleService;
import br.net.woodstock.rockframework.domain.ServiceException;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMFilter;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMRepository;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class LocaleServiceImpl implements LocaleService {

	private static final long	serialVersionUID					= -6181516238669651549L;

	// DEPARTMENT
	private static final String	JPQL_LIST_DEPARTMENT_BY_NAME		= "SELECT d FROM Department AS d WHERE d.name LIKE :name OR d.abbreviation LIKE :name ORDER BY d.name";

	private static final String	JPQL_COUNT_DEPARTMENT_BY_NAME		= "SELECT COUNT(*) FROM Department AS d WHERE d.name LIKE :name OR d.abbreviation LIKE :name";

	private static final String	JPQL_LIST_ROOT_DEPARTMENT			= "SELECT d FROM Department AS d WHERE d.parent IS NULL ORDER BY d.name";

	// DEPARTMENT
	private static final String	JPQL_LIST_DEPARTMENT_SKELL_BY_NAME	= "SELECT d FROM DepartmentSkell AS d WHERE d.name LIKE :name ORDER BY d.name";

	private static final String	JPQL_COUNT_DEPARTMENT_SKELL_BY_NAME	= "SELECT COUNT(*) FROM DepartmentSkell AS d WHERE d.name LIKE :name";

	private static final String	JPQL_LIST_ROOT_DEPARTMENT_SKELL		= "SELECT d FROM DepartmentSkell AS d WHERE d.parent IS NULL ORDER BY d.name";

	@Autowired(required = true)
	private ORMRepository		repository;

	public LocaleServiceImpl() {
		super();
	}

	// Department
	@Override
	public Department getDepartmentById(final Integer id) {
		try {
			return this.repository.get(Department.class, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveDepartment(final Department department) {
		try {
			department.setActive(Boolean.TRUE);
			this.repository.save(department);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateDepartment(final Department department) {
		try {
			Department r = this.repository.get(Department.class, department.getId());
			r.setAbbreviation(department.getAbbreviation());
			r.setActive(department.getActive());
			r.setName(department.getName());

			if (department.getParent() != null) {
				Department p = this.repository.get(Department.class, department.getParent().getId());
				r.setParent(p);
			} else {
				r.setParent(null);
			}

			if (department.getSkell() != null) {
				DepartmentSkell s = this.repository.get(DepartmentSkell.class, department.getSkell().getId());
				r.setSkell(s);
			} else {
				r.setParent(null);
			}

			this.repository.update(r);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ORMResult listDepartmentsByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", ORMRepositoryHelper.getLikeValue(name, false));

			ORMFilter filter = ORMRepositoryHelper.toORMFilter(LocaleServiceImpl.JPQL_LIST_DEPARTMENT_BY_NAME, LocaleServiceImpl.JPQL_COUNT_DEPARTMENT_BY_NAME, page, parameters);
			return this.repository.getCollection(filter);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<Department> listRootDepartments() {
		try {
			ORMFilter filter = ORMRepositoryHelper.toORMFilter(LocaleServiceImpl.JPQL_LIST_ROOT_DEPARTMENT);
			return this.repository.getCollection(filter).getItems();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	// DepartmentSkell
	@Override
	public DepartmentSkell getDepartmentSkellById(final Integer id) {
		try {
			return this.repository.get(DepartmentSkell.class, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveDepartmentSkell(final DepartmentSkell department) {
		try {
			this.repository.save(department);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateDepartmentSkell(final DepartmentSkell department) {
		try {
			DepartmentSkell r = this.repository.get(DepartmentSkell.class, department.getId());
			r.setName(department.getName());

			if (department.getParent() != null) {
				DepartmentSkell p = this.repository.get(DepartmentSkell.class, department.getParent().getId());
				r.setParent(p);
			} else {
				r.setParent(null);
			}

			this.repository.update(r);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ORMResult listDepartmentSkellsByName(final String name, final Page page) {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", ORMRepositoryHelper.getLikeValue(name, false));

			ORMFilter filter = ORMRepositoryHelper.toORMFilter(LocaleServiceImpl.JPQL_LIST_DEPARTMENT_SKELL_BY_NAME, LocaleServiceImpl.JPQL_COUNT_DEPARTMENT_SKELL_BY_NAME, page, parameters);
			return this.repository.getCollection(filter);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Collection<DepartmentSkell> listRootDepartmentSkells() {
		try {
			ORMFilter filter = ORMRepositoryHelper.toORMFilter(LocaleServiceImpl.JPQL_LIST_ROOT_DEPARTMENT_SKELL);
			return this.repository.getCollection(filter).getItems();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

}
