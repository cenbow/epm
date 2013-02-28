package br.net.woodstock.epm.web.security.user;

import br.net.woodstock.epm.orm.Department;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.web.AbstractForm;

public class UserRoleForm extends AbstractForm {

	private static final long	serialVersionUID	= -1245674591920940776L;

	private Integer				id;

	private User				user;

	private Role				role;

	private Department			department;

	private Boolean				active;

	public UserRoleForm() {
		super();
	}

	@Override
	public void reset() {
		this.setId(null);
		// this.setUser(null);
		this.setRole(null);
		this.setDepartment(null);
		this.setActive(null);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(final Role role) {
		this.role = role;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(final Department department) {
		this.department = department;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

}
