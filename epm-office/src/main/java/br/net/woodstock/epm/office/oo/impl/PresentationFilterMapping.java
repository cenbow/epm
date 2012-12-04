package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.FilterMapping;

public class PresentationFilterMapping implements FilterMapping {

	private static final long	serialVersionUID	= 5065842863697150486L;

	private List<String>		source;

	private List<String>		target;

	public PresentationFilterMapping() {
		super();
		this.source = new ArrayList<String>();
		this.source.add(OpenOfficeFilters.ODP);
		this.source.add(OpenOfficeFilters.ODP_TEMPLATE);
		this.source.add(OpenOfficeFilters.POWERPOINT_97);
		this.source.add(OpenOfficeFilters.POWERPOINT_2007);
		this.source.add(OpenOfficeFilters.POWERPOINT_2007_TEMPLATE);
		this.source.add(OpenOfficeFilters.SXI);
		this.source.add(OpenOfficeFilters.SXI_TEMPLATE);

		this.target = new ArrayList<String>();
		this.target.add(OpenOfficeFilters.ODP);
		this.target.add(OpenOfficeFilters.ODP_TEMPLATE);
		this.target.add(OpenOfficeFilters.POWERPOINT_97);
		this.target.add(OpenOfficeFilters.POWERPOINT_2007);
		this.target.add(OpenOfficeFilters.POWERPOINT_2007_TEMPLATE);
		this.target.add(OpenOfficeFilters.SXI);
		this.target.add(OpenOfficeFilters.SXI_TEMPLATE);
		// Export
		this.target.add(OpenOfficeFilters.PRESENTATION_TO_PDF);
		this.target.add(OpenOfficeFilters.PRESENTATION_TO_XHTML);
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
			case HTML:
				return OpenOfficeFilters.PRESENTATION_TO_XHTML;
			case ODP:
				return OpenOfficeFilters.ODP;
			case PDF:
				return OpenOfficeFilters.PRESENTATION_TO_PDF;
			case PPS:
				return OpenOfficeFilters.POWERPOINT_97;
			case PPSX:
				return OpenOfficeFilters.POWERPOINT_2007;
			case PPT:
				return OpenOfficeFilters.POWERPOINT_97;
			case PPTX:
				return OpenOfficeFilters.POWERPOINT_2007;
			case SXI:
				return OpenOfficeFilters.SXI;
			default:
				return null;
		}
	}

}
