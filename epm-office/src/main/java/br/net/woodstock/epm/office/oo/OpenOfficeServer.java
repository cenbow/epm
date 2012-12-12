package br.net.woodstock.epm.office.oo;

public interface OpenOfficeServer {

	void start();

	void stop();

	boolean isRunning();

	OpenOfficeConnection getConnection();

}
