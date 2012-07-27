package br.net.woodstock.epm.document.lucene;

import br.net.woodstock.epm.document.api.Field;

public enum LuceneField {

	CONTENT_TYPE(Field.CONTENT_TYPE, "CONTENT-TYPE"), /***/
	DATE(Field.DATE, "DATE"), /***/
	GROUP(Field.GROUP, "GROUP"), /***/
	ID(Field.ID, "ID"), /***/
	NAME(Field.NAME, "NAME"), /***/
	USER(Field.USER, "USER"), /***/
	VERSION(Field.VERSION, "VERSION"), /***/
	TEXT(Field.TEXT, "TEXT");

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
