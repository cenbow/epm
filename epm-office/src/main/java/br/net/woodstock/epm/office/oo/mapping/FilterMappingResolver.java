package br.net.woodstock.epm.office.oo.mapping;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.FilterMapping;

public final class FilterMappingResolver {

	private static FilterMappingResolver	instance			= new FilterMappingResolver();

	private FilterMapping					imageMapping		= new ImageFilterMapping();

	private FilterMapping					presentationMapping	= new PresentationFilterMapping();

	private FilterMapping					spreadSheetMapping	= new SpreadSheetFilterMapping();

	private FilterMapping					textMapping			= new TextFilterMapping();

	private FilterMappingResolver() {
		super();
	}

	public FilterMapping getFilterMapping(final String currentFilterName, final OfficeDocumentType targetType) {
		FilterMapping mapping = this.getFilterMappingBySourceFilterName(currentFilterName);

		if (mapping != null) {
			String targetFilter = mapping.getExportFilter(targetType);

			if (mapping.acceptTarget(targetFilter)) {
				return mapping;
			}
		}

		return null;
	}

	private FilterMapping getFilterMappingBySourceFilterName(final String filterName) {
		if (this.imageMapping.acceptSource(filterName)) {
			return this.imageMapping;
		}
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

	public static FilterMappingResolver getInstance() {
		return FilterMappingResolver.instance;
	}

}
