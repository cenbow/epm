package br.net.woodstock.epm.document.lucene;

public enum LuceneField {

	CREATED("CREATED"), /***/
	CREATED_DAY("CREATED-DAY"), /***/
	CREATED_MONTH("CREATED-MONTH"), /***/
	CREATED_YEAR("CREATED-YEAR"), /***/
	CREATED_DATE_LATIN("CREATED-DATE-LATIN"), /***/
	CREATED_DATE_US("CREATED-DATE-US"), /***/
	ID("ID"), /***/
	MIME_TYPE("MIME-TYPE"), /***/
	MODIFIED("MODIFIED"), /***/
	MODIFIED_DAY("MODIFIED-DAY"), /***/
	MODIFIED_MONTH("MODIFIED-MONTH"), /***/
	MODIFIED_YEAR("MODIFIED-YEAR"), /***/
	MODIFIED_DATE_LATIN("MODIFIED-DATE-LATIN"), /***/
	MODIFIED_DATE_US("MODIFIED-DATE-US"), /***/
	NAME("NAME"), /***/
	OWNER("OWNER"), /***/
	SCORE("SCORE"), /***/
	TEXT("TEXT");

	private String	name;

	private LuceneField(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
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
