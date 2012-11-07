package br.net.woodstock.epm.client.document;

import java.io.Serializable;
import java.util.Date;

import br.net.woodstock.epm.client.security.User;

public class Document implements Serializable {

	private static final long	serialVersionUID	= -9202527290321503603L;

	private String				id;

	private String				name;

	private String				mimeType;

	private Date				created;

	private Date				modified;

	private byte[]				binary;

	private String				text;

	private User				owner;

	public Document() {
		super();
	}

	public Document(final String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCreated(final Date created) {
		this.created = created;
	}

	public Date getModified() {
		return this.modified;
	}

	public void setModified(final Date modified) {
		this.modified = modified;
	}

	public byte[] getBinary() {
		return this.binary;
	}

	public void setBinary(final byte[] binary) {
		this.binary = binary;
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public User getOwner() {
		return this.owner;
	}

	public void setOwner(final User owner) {
		this.owner = owner;
	}

}
