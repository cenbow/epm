package br.net.woodstock.epm.bpmn2.beans;

import java.io.Serializable;

public abstract class FormItem implements Serializable {

	private static final long	serialVersionUID	= 1559549399387876455L;

	private String				id;

	private String				name;

	private FormItemType		type;

	private boolean				required;

	private boolean				readable;

	private boolean				writeable;

	public FormItem() {
		this(null, null, null);
	}

	public FormItem(final String id, final String name, final FormItemType type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.required = false;
		this.readable = true;
		this.writeable = true;
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

	public FormItemType getType() {
		return this.type;
	}

	public void setType(final FormItemType type) {
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

}
