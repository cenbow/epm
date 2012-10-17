package br.net.woodstock.epm.search.lucene;

import br.net.woodstock.epm.search.api.Field;

public enum LuceneField {

	CONTENT_TYPE(Field.CONTENT_TYPE, "CONTENT-TYPE"), /***/
	DATE(Field.DATE, "DATE"), /***/
	DESCRIPTION(Field.DESCRIPTION, "DESCRIPTION"), /***/
	EXTENSION(Field.DATE, "DATE"), /***/
	ID(Field.ID, "ID"), /***/
	NAME(Field.NAME, "NAME"), /***/
	OWNER(Field.OWNER, "OWNER"), /***/
	SCORE(null, "SCORE"), /***/
	TEXT(Field.TEXT, "TEXT"), /***/
	TYPE(Field.TYPE, "TYPE");

	private Field	field;

	private String	name;

	private LuceneField(final Field field, final String name) {
		this.field = field;
		this.name = name;
	}

	public Field getField() {
		return this.field;
	}

	public String getName() {
		return this.name;
	}

	public static LuceneField fromField(final Field field) {
		for (LuceneField f : LuceneField.values()) {
			if (f.getField().equals(field)) {
				return f;
			}
		}
		return null;
	}

	public static LuceneField fromName(final String name) {
		for (LuceneField f : LuceneField.values()) {
			if (f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}

}
