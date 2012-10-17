package br.net.woodstock.epm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EPMLog {

	private static final String	NAME	= "br.net.woodstock.epm";

	private static final Logger	LOGGER	= LoggerFactory.getLogger(EPMLog.NAME);

	private EPMLog() {
		super();
	}

	public static Logger getLogger() {
		return EPMLog.LOGGER;
	}

}
