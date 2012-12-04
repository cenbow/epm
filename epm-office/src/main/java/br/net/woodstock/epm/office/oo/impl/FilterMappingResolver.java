package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.FilterMapping;

public class FilterMappingResolver {

	private static FilterMappingResolver	instance			= new FilterMappingResolver();

	private FilterMapping					presentationMapping	= new PresentationFilterMapping();

	private FilterMapping					spreadSheetMapping	= new SpreadSheetFilterMapping();

	private FilterMapping					textMapping			= new TextFilterMapping();

	private FilterMappingResolver() {
		super();
	}

	public FilterMapping getFilterMapping(final String currentFilterName, final OfficeDocumentType targetType) {
		FilterMapping mapping = this.getFilterMappingBySourceFilterName(currentFilterName);

		if (mapping != null) {
			String targetFilter = null;

			if (targetType == OfficeDocumentType.HTML) {
				String [] s = mapping.getTarget();
			} else if (targetType == OfficeDocumentType.PDF) {

			} else {
				targetFilter = this.getFilterByDocumentType(targetType);
			}

			if (mapping.acceptTarget(targetFilter)) {
				return mapping;
			}
		}

		return null;
	}

	private FilterMapping getFilterMappingBySourceFilterName(final String filterName) {
		if (this.presentationMapping.acceptSource(filterName)) {
			return this.presentationMapping;
		}
		if (this.spreadSheetMapping.acceptSource(filterName)) {
			return this.spreadSheetMapping;
		}
		if (this.textMapping.acceptSource(filterName)) {
			return this.textMapping;
		}
		return null;
	}

	private String getFilterByDocumentType(final OfficeDocumentType targetType) {
		switch (targetType) {
			case CSV:
				return OpenOfficeFilters.CSV;
			case DOC:
				return OpenOfficeFilters.WORD_97;
			case DOCX:
				return OpenOfficeFilters.WORD_2007;
			case HTML:
				return null; // Export
			case ODP:
				return OpenOfficeFilters.ODP;
			case ODS:
				return OpenOfficeFilters.ODS;
			case ODT:
				return OpenOfficeFilters.ODT;
			case PDF:
				return null; // Export
			case PPS:
				return OpenOfficeFilters.POWERPOINT_97;
			case PPSX:
				return OpenOfficeFilters.POWERPOINT_2007;
			case PPT:
				return OpenOfficeFilters.POWERPOINT_97;
			case PPTX:
				return OpenOfficeFilters.POWERPOINT_2007;
			case RTF:
				return OpenOfficeFilters.RTF;
			case SXC:
				return OpenOfficeFilters.SXC;
			case SXI:
				return OpenOfficeFilters.SXI;
			case SXW:
				return OpenOfficeFilters.SXW;
			case TXT:
				return OpenOfficeFilters.TEXT;
			case XLS:
				return OpenOfficeFilters.EXCEL_97;
			case XLSX:
				return OpenOfficeFilters.EXCEL_2007;
			default:
				return null;
		}
	}

	public static FilterMappingResolver getInstance() {
		return FilterMappingResolver.instance;
	}

}
