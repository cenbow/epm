package br.net.woodstock.epm.web.security.resource;

import br.net.woodstock.epm.web.AbstractForm;

public class ResourceForm extends AbstractForm {

	private static final long	serialVersionUID	= 2323654355027520966L;

	private Integer				id;

	private String				name;

	private Boolean				active;

	public ResourceForm() {
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
