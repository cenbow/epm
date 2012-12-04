package br.net.woodstock.epm.office.oo;

import java.io.Serializable;

public interface FilterMapping extends Serializable {

	String[] getSource();

	String[] getTarget();

	boolean acceptSource(String filterName);

	boolean acceptTarget(String filterName);

}
