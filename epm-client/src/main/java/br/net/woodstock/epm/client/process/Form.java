package br.net.woodstock.epm.client.process;

import java.io.Serializable;

public class Form implements Serializable {

	private static final long	serialVersionUID	= -6313964172223116261L;

	private String				id;

	private FormField[]			fields;

	public Form() {
		super();
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public FormField[] getFields() {
		return this.fields;
	}

	public void setFields(final FormField[] fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return this.getId();
	}

}
