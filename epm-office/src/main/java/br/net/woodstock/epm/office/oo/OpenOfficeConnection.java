package br.net.woodstock.epm.office.oo;

public interface OpenOfficeConnection {

	boolean isConnected();

	void connect();

	void close();

	Object getDelegate();

}
