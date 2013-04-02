package br.net.woodstock.epm.web.tree;

import java.io.Serializable;

import br.net.woodstock.rockframework.core.util.EqualsBuilder;
import br.net.woodstock.rockframework.core.util.HashCodeBuilder;

public class TreeItem implements Serializable {

	private static final long	serialVersionUID	= -4572826653478013987L;

	private Integer				id;

	private String				name;

	private String				description;

	public TreeItem() {
		super();
	}

	public TreeItem(final Integer id, final String name, final String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().add(this.getId()).add(this.getName()).add(this.getDescription()).build().intValue();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof TreeItem) {
			TreeItem other = (TreeItem) obj;
			return new EqualsBuilder().add(this.getId(), other.getId()).add(this.getName(), other.getName()).add(this.getDescription(), other.getDescription()).build().booleanValue();
		}
		return false;
	}

}
