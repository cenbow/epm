package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.FilterMapping;

class SpreadSheetFilterMapping implements FilterMapping {

	private static final long	serialVersionUID	= 5065842863697150486L;

	private List<String>		source;

	private List<String>		target;

	public SpreadSheetFilterMapping() {
		super();
		this.source = new ArrayList<String>();
		this.source.add(OpenOfficeFilters.CSV);
		this.source.add(OpenOfficeFilters.EXCEL_97);
		this.source.add(OpenOfficeFilters.EXCEL_2003);
		this.source.add(OpenOfficeFilters.EXCEL_2007);
		this.source.add(OpenOfficeFilters.EXCEL_2007_TEMPLATE);
		this.source.add(OpenOfficeFilters.ODS);
		this.source.add(OpenOfficeFilters.ODS_TEMPLATE);
		this.source.add(OpenOfficeFilters.SXC);
		this.source.add(OpenOfficeFilters.SXC_TEMPLATE);

		this.target = new ArrayList<String>();
		this.target.add(OpenOfficeFilters.CSV);
		this.target.add(OpenOfficeFilters.EXCEL_97);
		this.target.add(OpenOfficeFilters.EXCEL_2003);
		this.target.add(OpenOfficeFilters.EXCEL_2007);
		this.target.add(OpenOfficeFilters.EXCEL_2007_TEMPLATE);
		this.target.add(OpenOfficeFilters.ODS);
		this.target.add(OpenOfficeFilters.ODS_TEMPLATE);
		this.target.add(OpenOfficeFilters.SXC);
		this.target.add(OpenOfficeFilters.SXC_TEMPLATE);
		// Export
		this.target.add(OpenOfficeFilters.SPREADSHEET_TO_PDF);
		this.target.add(OpenOfficeFilters.SPREADSHEET_TO_XHTML);
	}

	@Override
	public boolean acceptSource(final String filterName) {
		return this.source.contains(filterName);
	}

	@Override
	public boolean acceptTarget(final String filterName) {
		return this.target.contains(filterName);
	}

	@Override
	public String getExportFilter(final OfficeDocumentType documentType) {
		switch (documentType) {
			case CSV:
				return OpenOfficeFilters.CSV;
			case HTML:
				return null; // Export
			case ODS:
				return OpenOfficeFilters.ODS;
			case PDF:
				return null; // Export
			case SXC:
				return OpenOfficeFilters.SXC;
			case XLS:
				return OpenOfficeFilters.EXCEL_97;
			case XLSX:
				return OpenOfficeFilters.EXCEL_2007;
			default:
				return null;
		}
	}

}
