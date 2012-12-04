package br.net.woodstock.epm.office.oo;

import java.io.Serializable;

import br.net.woodstock.epm.office.OfficeDocumentType;

public interface FilterMapping extends Serializable {

	boolean acceptSource(String filterName);

	boolean acceptTarget(String filterName);

	String getExportFilter(OfficeDocumentType documentType);

}
