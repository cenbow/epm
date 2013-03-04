package br.net.woodstock.epm.web.security.departmentSkell;

import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.epm.web.AbstractForm;

public class DepartmentSkellForm extends AbstractForm {

	private static final long	serialVersionUID	= -1976299756804938146L;

	private Integer				id;

	private String				name;

	private DepartmentSkell		parent;

	public DepartmentSkellForm() {
		super();
	}

	@Override
	public void reset() {
		this.setId(null);
		this.setName(null);
		this.setParent(null);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public DepartmentSkell getParent() {
		return this.parent;
	}

	public void setParent(final DepartmentSkell parent) {
		this.parent = parent;
	}

}
