package br.net.woodstock.epm.orm;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import br.net.woodstock.rockframework.domain.persistence.AbstractIntegerEntity;

@Entity
@Table(name = "epm_user")
@Indexed
public class User extends AbstractIntegerEntity {

	private static final long	serialVersionUID	= -1932408409262519409L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer				id;

	@Column(name = "user_login", length = 50, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 100)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String				login;

	@Column(name = "user_name", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String				name;

	@Column(name = "user_email", length = 100, nullable = false, unique = true)
	@NotNull
	@Size(min = 1, max = 100)
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String				email;

	@Column(name = "user_password", length = 100, nullable = false)
	@NotNull
	@Size(min = 1, max = 100)
	private String				password;

	@Column(name = "user_status", nullable = false, columnDefinition = "BIT")
	@NotNull
	private Boolean				active;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<UserRole>		roles;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	@IndexedEmbedded
	private Set<Certificate>	certificates;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private Set<Document>		documents;

	public User() {
		super();
	}

	public User(final Integer id) {
		super();
		this.id = id;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
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

	public Set<UserRole> getRoles() {
		return this.roles;
	}

	public void setRoles(final Set<UserRole> roles) {
		this.roles = roles;
	}

	public Set<Certificate> getCertificates() {
		return this.certificates;
	}

	public void setCertificates(final Set<Certificate> certificates) {
		this.certificates = certificates;
	}

	public Set<Document> getDocuments() {
		return this.documents;
	}

	public void setDocuments(final Set<Document> documents) {
		this.documents = documents;
	}

}
