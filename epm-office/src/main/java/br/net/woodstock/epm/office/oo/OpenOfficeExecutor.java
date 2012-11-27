package br.net.woodstock.epm.office.oo;

import com.sun.star.frame.XComponentLoader;

public interface OpenOfficeExecutor {

	<T> T doInConnection(XComponentLoader componentLoader);

}