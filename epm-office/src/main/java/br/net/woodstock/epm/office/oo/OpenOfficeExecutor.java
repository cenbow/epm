package br.net.woodstock.epm.office.oo;

public interface OpenOfficeExecutor {

	<T> T doInConnection(OpenOfficeConnection connection);

}