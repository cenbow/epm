package br.net.woodstock.epm.acl.api;

import java.io.Serializable;
import java.util.Date;

public class Certificate implements Serializable {

	private static final long	serialVersionUID	= -6090362987482017974L;

	private String				id;

	private String				alias;

	private String				serialNumber;

	private String				hash;

	private String				subject;

	private String				issuer;

	private String				algorithm;

	private byte[]				data;

	private byte[]				pkcs12;

	private Date				notBefore;

	private Date				notAfter;

	private Boolean				active;

	public Certificate() {
		super();
	}

	public Certificate(final String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
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

}
