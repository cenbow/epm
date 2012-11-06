package br.net.woodstock.epm.orm;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "epm_resource")
public class Resource implements Serializable {

	private static final long	serialVersionUID	= -4698111874290305730L;

	@Id
	@Column(name = "resource_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "resource_name", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String				name;

	@Column(name = "resource_status", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private Boolean				active;

	@ManyToMany(mappedBy = "resources", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<Role>			roles;

	public Resource() {
		super();
	}

	public Resource(final Integer id) {
		super();
		this.id = id;
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

	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(final Set<Role> roles) {
		this.roles = roles;
	}

}
