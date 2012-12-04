package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.oo.FilterMapping;
import br.net.woodstock.rockframework.utils.CollectionUtils;

class SpreadSheetFilterMapping implements FilterMapping {

	private static final long		serialVersionUID	= 5065842863697150486L;

	private static final String[]	SOURCE;

	private static final String[]	TARGET;

	static {
		List<String> list = new ArrayList<String>();
		list.add(OpenOfficeFilters.CSV);
		list.add(OpenOfficeFilters.EXCEL_97);
		list.add(OpenOfficeFilters.EXCEL_2003);
		list.add(OpenOfficeFilters.EXCEL_2007);
		list.add(OpenOfficeFilters.EXCEL_2007_TEMPLATE);
		list.add(OpenOfficeFilters.ODS);
		list.add(OpenOfficeFilters.ODS_TEMPLATE);
		list.add(OpenOfficeFilters.SXC);
		list.add(OpenOfficeFilters.SXC_TEMPLATE);
		SOURCE = CollectionUtils.toArray(list, String.class);
	}

	static {
		List<String> list = new ArrayList<String>();
		list.add(OpenOfficeFilters.CSV);
		list.add(OpenOfficeFilters.EXCEL_97);
		list.add(OpenOfficeFilters.EXCEL_2003);
		list.add(OpenOfficeFilters.EXCEL_2007);
		list.add(OpenOfficeFilters.EXCEL_2007_TEMPLATE);
		list.add(OpenOfficeFilters.ODS);
		list.add(OpenOfficeFilters.ODS_TEMPLATE);
		list.add(OpenOfficeFilters.SXC);
		list.add(OpenOfficeFilters.SXC_TEMPLATE);
		// Export
		list.add(OpenOfficeFilters.SPREADSHEET_TO_PDF);
		list.add(OpenOfficeFilters.SPREADSHEET_TO_XHTML);
		TARGET = CollectionUtils.toArray(list, String.class);
	}

	public SpreadSheetFilterMapping() {
		super();
	}

	@Override
	public String[] getSource() {
		return SpreadSheetFilterMapping.SOURCE;
	}

	@Override
	public String[] getTarget() {
		return SpreadSheetFilterMapping.TARGET;
	}

}
