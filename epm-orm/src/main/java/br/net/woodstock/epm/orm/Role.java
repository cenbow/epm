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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "epm_role")
public class Role implements Serializable {

	private static final long	serialVersionUID	= -1932408409262519409L;

	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "role_name", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String				name;

	@Column(name = "role_status", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private Boolean				active;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinTable(name = "epm_resource_role", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"), inverseJoinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "resource_id"))
	private Set<Resource>		resources;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<User>			users;

	public Role() {
		super();
	}

	public Role(final Integer id) {
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

	public Set<Resource> getResources() {
		return this.resources;
	}

	public void setResources(final Set<Resource> resources) {
		this.resources = resources;
	}

	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(final Set<User> users) {
		this.users = users;
	}

}
