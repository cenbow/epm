package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.rockframework.utils.ConditionUtils;

public abstract class OpenOfficeHelper {

	public static final String	INPUT_STREAM_PROPERTY			= "InputStream";

	public static final String	OUTPUT_STREAM_PROPERTY			= "OutputStream";

	public static final String	HIDDEN_PROPERTY					= "Hidden";

	public static final String	FILTER_NAME_PROPERTY			= "FilterName";

	public static final String	AS_TEMPLATE_PROPERTY			= "AsTemplate";

	public static final String	CONTENT_PROPERTY				= "Content";

	public static final String	DEPENDENT_TEXT_FIELD_PROPERTY	= "DependentTextFields";

	public static final String	PRIVATE_STREAM_URL				= "private:stream";

	public static final String	BLANK_TARGET					= "_blank";

	public static final String	FIELD_NAME_PREFIX				= "com.sun.star.text.fieldmaster.user.";

	private OpenOfficeHelper() {
		//
	}

	public static String getFilter(final String currentFilterName, final OfficeDocumentType targetType) {
		if(ConditionUtils.isNotEmpty(currentFilterName)) {
			
		}
		switch (targetType) {
			case DOC:
				return Filters.WORD_2003;
			case DOCX:
				return Filters.WORD_2007;
			case HTML:
				return Filters.ODT_XHTML;
			case PDF:
				return Filters.ODT_PDF;
			case RTF:
				return Filters.RTF;
			default:
				return Filters.ODT;
		}
	}

	public static boolean isUserField(final String name) {
		if (name.toLowerCase().startsWith(OpenOfficeHelper.FIELD_NAME_PREFIX)) {
			return true;
		}
		return false;
	}

	public static String getUserFieldName(final String name) {
		return name.substring(OpenOfficeHelper.FIELD_NAME_PREFIX.length());
	}

}
