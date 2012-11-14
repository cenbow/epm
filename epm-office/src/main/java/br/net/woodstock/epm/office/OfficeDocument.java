package br.net.woodstock.epm.office;

import java.io.Serializable;

public class OfficeDocument implements Serializable {

	private static final long	serialVersionUID	= -5549137091225061658L;

	private String				name;

	private byte[]				content;

	private OfficeDocumentType	type;

	public OfficeDocument() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}

	public OfficeDocumentType getType() {
		return this.type;
	}

	public void setType(final OfficeDocumentType type) {
		this.type = type;
	}

}
