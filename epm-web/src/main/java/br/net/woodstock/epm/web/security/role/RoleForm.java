package br.net.woodstock.epm.web.security.role;

import br.net.woodstock.epm.web.AbstractForm;

public class RoleForm extends AbstractForm {

	private static final long	serialVersionUID	= 2193012230532194731L;

	private Integer				id;

	private String				name;

	private Boolean				active;

	public RoleForm() {
		super();
	}

	@Override
	public void reset() {
		this.setActive(null);
		this.setId(null);
		this.setName(null);
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

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

}
