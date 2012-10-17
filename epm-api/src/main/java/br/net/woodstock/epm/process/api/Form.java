package br.net.woodstock.epm.process.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Form implements Serializable {

	private static final long		serialVersionUID	= -6313964172223116261L;

	private String					id;

	private Map<String, FormField>	fields;

	public Form() {
		super();
		this.fields = new HashMap<String, FormField>();
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Map<String, FormField> getFields() {
		return this.fields;
	}

	public void setFields(final Map<String, FormField> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return this.getId();
	}

	// Aux
	public FormField getField(final String id) {
		return this.fields.get(id);
	}

}
