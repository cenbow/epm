package br.net.woodstock.epm.office.oo.mapping;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.FilterMapping;

public class TextFilterMapping implements FilterMapping {

	private static final long	serialVersionUID	= 5065842863697150486L;

	private List<String>		source;

	private List<String>		target;

	public TextFilterMapping() {
		super();
		this.source = new ArrayList<String>();
		this.source.add(OpenOfficeFilters.ODT);
		this.source.add(OpenOfficeFilters.ODT_TEMPLATE);
		this.source.add(OpenOfficeFilters.RTF);
		this.source.add(OpenOfficeFilters.SXW);
		this.source.add(OpenOfficeFilters.SXW_TEMPLATE);
		this.source.add(OpenOfficeFilters.TEXT);
		this.source.add(OpenOfficeFilters.WORD_97);
		this.source.add(OpenOfficeFilters.WORD_2003);
		this.source.add(OpenOfficeFilters.WORD_2007);
		this.source.add(OpenOfficeFilters.WORD_2007_TEMPLATE);

		this.target = new ArrayList<String>();
		this.target.add(OpenOfficeFilters.ODT);
		this.target.add(OpenOfficeFilters.ODT_TEMPLATE);
		this.target.add(OpenOfficeFilters.RTF);
		this.target.add(OpenOfficeFilters.SXW);
		this.target.add(OpenOfficeFilters.SXW_TEMPLATE);
		this.target.add(OpenOfficeFilters.TEXT);
		this.target.add(OpenOfficeFilters.WORD_97);
		this.target.add(OpenOfficeFilters.WORD_2003);
		this.target.add(OpenOfficeFilters.WORD_2007);
		this.target.add(OpenOfficeFilters.WORD_2007_TEMPLATE);
		// Export
		this.target.add(OpenOfficeFilters.TEXT_DOCUMENT_TO_PDF);
		this.target.add(OpenOfficeFilters.TEXT_DOCUMENT_TO_XHTML);
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
			case DOC:
				return OpenOfficeFilters.WORD_97;
			case DOCX:
				return OpenOfficeFilters.WORD_2007;
			case HTML:
				return OpenOfficeFilters.TEXT_DOCUMENT_TO_XHTML;
			case ODT:
				return OpenOfficeFilters.ODT;
			case PDF:
				return OpenOfficeFilters.TEXT_DOCUMENT_TO_PDF;
			case RTF:
				return OpenOfficeFilters.RTF;
			case SXW:
				return OpenOfficeFilters.SXW;
			case TXT:
				return OpenOfficeFilters.TEXT;
			default:
				return null;

		}
	}

}
