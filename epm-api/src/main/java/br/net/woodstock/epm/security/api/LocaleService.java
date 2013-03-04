package br.net.woodstock.epm.security.api;

import br.net.woodstock.epm.orm.Department;
import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.rockframework.domain.service.Service;
import br.net.woodstock.rockframework.persistence.orm.Page;
import br.net.woodstock.rockframework.persistence.orm.QueryResult;

public interface LocaleService extends Service {

	// Department
	Department getDepartmentById(Integer id);

	void saveDepartment(Department department);

	void updateDepartment(Department department);

	QueryResult listDepartmentsByName(String name, Page page);

	// Department
	DepartmentSkell getDepartmentSkellById(Integer id);

	void saveDepartmentSkell(DepartmentSkell department);

	void updateDepartmentSkell(DepartmentSkell department);

	QueryResult listDepartmentsSkellByName(String name, Page page);

}
