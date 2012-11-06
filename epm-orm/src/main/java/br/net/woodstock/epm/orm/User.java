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
@Table(name = "epm_user")
public class User implements Serializable {

	private static final long	serialVersionUID	= -1932408409262519409L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "user_login", length = 50, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String				login;

	@Column(name = "user_name", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String				name;

	@Column(name = "user_email", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String				email;

	@Column(name = "user_password", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String				password;

	@Column(name = "user_status", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private Boolean				active;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@JoinTable(name = "epm_user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
	private Set<Role>			roles;

	private Set<Certificate>	certificates;

	public User() {
		super();
	}

	public User(final Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
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

	public Set<Certificate> getCertificates() {
		return this.certificates;
	}

	public void setCertificates(final Set<Certificate> certificates) {
		this.certificates = certificates;
	}

}