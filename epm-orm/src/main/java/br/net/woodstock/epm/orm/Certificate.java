package br.net.woodstock.epm.orm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "epm_certificate")
public class Certificate implements Serializable {

	private static final long	serialVersionUID	= 8906036120025996121L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "certificate_id", unique = true)
	private Integer				id;

	@Column(name = "certificate_alias", length = 45, nullable = false)
	@NotNull
	@Size(min = 1, max = 45)
	private String				alias;

	@Column(name = "certificate_serial_number", length = 255, nullable = false)
	@NotNull
	@Size(min = 1, max = 255)
	private String				serialNumber;

	@Column(name = "certificate_hash", length = 255, nullable = false)
	@NotNull
	@Size(min = 1, max = 255)
	private String				hash;

	@Column(name = "certificate_subject", length = 255, nullable = false)
	@NotNull
	@Size(min = 1, max = 255)
	private String				subject;

	@Column(name = "certificate_issuer", length = 255, nullable = false)
	@NotNull
	@Size(min = 1, max = 255)
	private String				issuer;

	@Column(name = "certificate_algorithm", length = 45, nullable = false)
	@NotNull
	@Size(min = 1, max = 45)
	private String				algorithm;

	@Column(name = "certificate_data", nullable = false)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@NotNull
	private byte[]				data;

	@Column(name = "certificate_pkcs12", nullable = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[]				pkcs12;

	@Column(name = "certificate_not_before", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				notBefore;

	@Column(name = "certificate_not_after", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				notAfter;

	@Column(name = "certificate_status", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private Boolean				active;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	@NotNull
	private User				user;

	public Certificate() {
		super();
	}

	public Certificate(final Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(final String alias) {
		this.alias = alias;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(final String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getHash() {
		return this.hash;
	}

	public void setHash(final String hash) {
		this.hash = hash;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getIssuer() {
		return this.issuer;
	}

	public void setIssuer(final String issuer) {
		this.issuer = issuer;
	}

	public String getAlgorithm() {
		return this.algorithm;
	}

	public void setAlgorithm(final String algorithm) {
		this.algorithm = algorithm;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(final byte[] data) {
		this.data = data;
	}

	public byte[] getPkcs12() {
		return this.pkcs12;
	}

	public void setPkcs12(final byte[] pkcs12) {
		this.pkcs12 = pkcs12;
	}

	public Date getNotBefore() {
		return this.notBefore;
	}

	public void setNotBefore(final Date notBefore) {
		this.notBefore = notBefore;
	}

	public Date getNotAfter() {
		return this.notAfter;
	}

	public void setNotAfter(final Date notAfter) {
		this.notAfter = notAfter;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

}
