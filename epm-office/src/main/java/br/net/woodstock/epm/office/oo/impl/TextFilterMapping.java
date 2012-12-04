package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.oo.FilterMapping;
import br.net.woodstock.rockframework.utils.CollectionUtils;

class TextFilterMapping implements FilterMapping {

	private static final long		serialVersionUID	= 5065842863697150486L;

	private static final String[]	SOURCE;

	private static final String[]	TARGET;

	static {
		List<String> list = new ArrayList<String>();
		list.add(OpenOfficeFilters.ODT);
		list.add(OpenOfficeFilters.ODT_TEMPLATE);
		list.add(OpenOfficeFilters.RTF);
		list.add(OpenOfficeFilters.SXW);
		list.add(OpenOfficeFilters.SXW_TEMPLATE);
		list.add(OpenOfficeFilters.TEXT);
		list.add(OpenOfficeFilters.WORD_97);
		list.add(OpenOfficeFilters.WORD_2003);
		list.add(OpenOfficeFilters.WORD_2007);
		list.add(OpenOfficeFilters.WORD_2007_TEMPLATE);
		SOURCE = CollectionUtils.toArray(list, String.class);
	}

	static {
		List<String> list = new ArrayList<String>();
		list.add(OpenOfficeFilters.ODT);
		list.add(OpenOfficeFilters.ODT_TEMPLATE);
		list.add(OpenOfficeFilters.RTF);
		list.add(OpenOfficeFilters.SXW);
		list.add(OpenOfficeFilters.SXW_TEMPLATE);
		list.add(OpenOfficeFilters.TEXT);
		list.add(OpenOfficeFilters.WORD_97);
		list.add(OpenOfficeFilters.WORD_2003);
		list.add(OpenOfficeFilters.WORD_2007);
		list.add(OpenOfficeFilters.WORD_2007_TEMPLATE);
		// Export
		list.add(OpenOfficeFilters.TEXT_DOCUMENT_TO_PDF);
		list.add(OpenOfficeFilters.TEXT_DOCUMENT_TO_XHTML);
		TARGET = CollectionUtils.toArray(list, String.class);
	}

	public TextFilterMapping() {
		super();
	}

	@Override
	public String[] getSource() {
		return TextFilterMapping.SOURCE;
	}

	@Override
	public String[] getTarget() {
		return TextFilterMapping.TARGET;
	}

}
