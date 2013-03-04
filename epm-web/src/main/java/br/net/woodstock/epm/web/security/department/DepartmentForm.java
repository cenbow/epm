package br.net.woodstock.epm.web.security.department;

import br.net.woodstock.epm.orm.Department;
import br.net.woodstock.epm.orm.DepartmentSkell;
import br.net.woodstock.epm.web.AbstractForm;

public class DepartmentForm extends AbstractForm {

	private static final long	serialVersionUID	= 6243763629739870386L;

	private Integer				id;

	private String				abbreviation;

	private String				name;

	private Boolean				active;

	private Department			parent;

	private DepartmentSkell		skell;

	public DepartmentForm() {
		super();
	}

	@Override
	public void reset() {
		this.setAbbreviation(null);
		this.setActive(null);
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

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public void setAbbreviation(final String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Department getParent() {
		return this.parent;
	}

	public void setParent(final Department parent) {
		this.parent = parent;
	}

	public DepartmentSkell getSkell() {
		return this.skell;
	}

	public void setSkell(final DepartmentSkell skell) {
		this.skell = skell;
	}

}
