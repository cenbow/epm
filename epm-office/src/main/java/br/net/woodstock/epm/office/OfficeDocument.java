package br.net.woodstock.epm.office;

import java.io.Serializable;

public class OfficeDocument implements Serializable {

	private static final long	serialVersionUID	= -5549137091225061658L;

	private OfficeDocumentType	type;

	public OfficeDocument() {
		super();
	}

	public OfficeDocumentType getType() {
		return this.type;
	}

	public void setType(final OfficeDocumentType type) {
		this.type = type;
	}

}
