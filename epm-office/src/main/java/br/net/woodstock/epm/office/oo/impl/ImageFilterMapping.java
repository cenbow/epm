package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.FilterMapping;

public class ImageFilterMapping implements FilterMapping {

	private static final long	serialVersionUID	= 5065842863697150486L;

	private List<String>		source;

	private List<String>		target;

	public ImageFilterMapping() {
		super();
		this.source = new ArrayList<String>();
		this.source.add(OpenOfficeFilters.DRAW_PDF);
		this.source.add(OpenOfficeFilters.ODG);
		this.source.add(OpenOfficeFilters.ODG_TEMPLATE);
		this.source.add(OpenOfficeFilters.SXD);
		this.source.add(OpenOfficeFilters.SXD_TEMPLATE);

		this.target = new ArrayList<String>();
		this.source.add(OpenOfficeFilters.DRAW_PDF);
		this.source.add(OpenOfficeFilters.ODG);
		this.source.add(OpenOfficeFilters.ODG_TEMPLATE);
		this.source.add(OpenOfficeFilters.SXD);
		this.source.add(OpenOfficeFilters.SXD_TEMPLATE);
		// Export
		this.target.add(OpenOfficeFilters.DRAW_TO_PDF);
		this.target.add(OpenOfficeFilters.DRAW_TO_XHTML);
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
				return OpenOfficeFilters.DRAW_TO_XHTML;
			case PDF:
				return OpenOfficeFilters.DRAW_TO_PDF;
			default:
				return null;

		}
	}

}
