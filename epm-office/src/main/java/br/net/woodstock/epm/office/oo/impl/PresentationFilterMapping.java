package br.net.woodstock.epm.office.oo.impl;

import java.util.ArrayList;
import java.util.List;

import br.net.woodstock.epm.office.oo.FilterMapping;
import br.net.woodstock.rockframework.utils.CollectionUtils;

class PresentationFilterMapping implements FilterMapping {

	private static final long		serialVersionUID	= 5065842863697150486L;

	private static final String[]	SOURCE;

	private static final String[]	TARGET;

	static {
		List<String> list = new ArrayList<String>();
		list.add(OpenOfficeFilters.ODP);
		list.add(OpenOfficeFilters.ODP_TEMPLATE);
		list.add(OpenOfficeFilters.POWERPOINT_97);
		list.add(OpenOfficeFilters.POWERPOINT_2007);
		list.add(OpenOfficeFilters.POWERPOINT_2007_TEMPLATE);
		list.add(OpenOfficeFilters.SXI);
		list.add(OpenOfficeFilters.SXI_TEMPLATE);
		SOURCE = CollectionUtils.toArray(list, String.class);
	}

	static {
		List<String> list = new ArrayList<String>();
		list.add(OpenOfficeFilters.ODP);
		list.add(OpenOfficeFilters.ODP_TEMPLATE);
		list.add(OpenOfficeFilters.POWERPOINT_97);
		list.add(OpenOfficeFilters.POWERPOINT_2007);
		list.add(OpenOfficeFilters.POWERPOINT_2007_TEMPLATE);
		list.add(OpenOfficeFilters.SXI);
		list.add(OpenOfficeFilters.SXI_TEMPLATE);
		// Export
		list.add(OpenOfficeFilters.PRESENTATION_TO_PDF);
		list.add(OpenOfficeFilters.PRESENTATION_TO_XHTML);
		TARGET = CollectionUtils.toArray(list, String.class);
	}

	public PresentationFilterMapping() {
		super();
	}

	@Override
	public String[] getSource() {
		return PresentationFilterMapping.SOURCE;
	}

	@Override
	public String[] getTarget() {
		return PresentationFilterMapping.TARGET;
	}

}
