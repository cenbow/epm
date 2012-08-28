package br.net.woodstock.epm.search.api;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long	serialVersionUID	= 1598068771825652675L;

	private String				id;

	private String				name;

	private String				description;

	private String				type;

	private String				contentType;

	private String				extension;

	private String				text;

	private String				owner;

	private String				date;

	public Item() {
		super();
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension(final String extension) {
		this.extension = extension;
	}

	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(final String owner) {
		this.owner = owner;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(final String date) {
		this.date = date;
	}

}
