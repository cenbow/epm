package br.net.woodstock.epm.office.oo;

public interface OpenOfficeManager {

	OpenOfficeConnection getConnection();

	<T> T execute(OpenOfficeExecutor executor);

}
