package br.net.woodstock.epm.process.api;

import java.io.Serializable;

public class FormField implements Serializable {

	private static final long	serialVersionUID	= 5838758698454483203L;

	private String				id;

	private String				name;

	private String				value;

	private String				type;

	private boolean				required;

	private boolean				readable;

	private boolean				writeable;

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

	public String getValue() {
		return this.value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setRequired(final boolean required) {
		this.required = required;
	}

	public boolean isReadable() {
		return this.readable;
	}

	public void setReadable(final boolean readable) {
		this.readable = readable;
	}

	public boolean isWriteable() {
		return this.writeable;
	}

	public void setWriteable(final boolean writeable) {
		this.writeable = writeable;
	}

	@Override
	public String toString() {
		return this.getId();
	}

}
