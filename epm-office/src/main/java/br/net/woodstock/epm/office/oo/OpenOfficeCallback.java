package br.net.woodstock.epm.office.oo;

public interface OpenOfficeCallback<T> {

	T doInConnection(OpenOfficeConnection connection);

}