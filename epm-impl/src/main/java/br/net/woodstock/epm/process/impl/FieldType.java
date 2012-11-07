package br.net.woodstock.epm.process.impl;

public enum FieldType {

	BOOLEAN("boolean"), /***/
	DATE("date"), /***/
	ENUM("enum"), /***/
	LONG("long"), /***/
	STRING("string");

	private String	type;

	private FieldType(final String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public static FieldType fromType(final String type) {
		for (FieldType t : FieldType.values()) {
			if (t.getType().equalsIgnoreCase(type)) {
				return t;
			}
		}
		return null;
	}

}
