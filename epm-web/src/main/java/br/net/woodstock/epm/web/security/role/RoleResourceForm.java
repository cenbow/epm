package br.net.woodstock.epm.web.security.role;

import org.primefaces.model.DualListModel;

import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.web.AbstractForm;

public class RoleResourceForm extends AbstractForm {

	private static final long		serialVersionUID	= 4838409927924349462L;

	private Integer					id;

	private Role					role;

	private DualListModel<Resource>	resources;

	public RoleResourceForm() {
		super();
	}

	@Override
	public void reset() {
		this.setId(null);
		// this.setRole(null);
		this.setResources(null);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(final Role role) {
		this.role = role;
	}

	public DualListModel<Resource> getResources() {
		return this.resources;
	}

	public void setResources(final DualListModel<Resource> resources) {
		this.resources = resources;
	}

}
