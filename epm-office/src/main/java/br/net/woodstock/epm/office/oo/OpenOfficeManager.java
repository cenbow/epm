package br.net.woodstock.epm.office.oo;

public interface OpenOfficeManager {

	<T> T execute(OpenOfficeCallback callback);

}
