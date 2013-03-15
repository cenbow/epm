package br.net.woodstock.epm.security.api;

import java.util.Collection;

import br.net.woodstock.epm.orm.Department;
import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.rockframework.domain.Service;
import br.net.woodstock.rockframework.domain.persistence.Page;
import br.net.woodstock.rockframework.domain.persistence.orm.ORMResult;

public interface LocaleService extends Service {

	// Department
	Department getDepartmentById(Integer id);

	void saveDepartment(Department department);

	void updateDepartment(Department department);

	ORMResult listDepartmentsByName(String name, Page page);

	Collection<Department> listRootDepartments();

	// Department
	DepartmentSkell getDepartmentSkellById(Integer id);

	void saveDepartmentSkell(DepartmentSkell department);

	void updateDepartmentSkell(DepartmentSkell department);

	ORMResult listDepartmentSkellsByName(String name, Page page);

	Collection<DepartmentSkell> listRootDepartmentSkells();

}
