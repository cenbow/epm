package br.net.woodstock.epm.api;

import org.apache.log4j.Logger;

public abstract class EPMLog {

	private static final String	LOG_NAME	= "br.net.woodstock.epm";

	private static Logger		log			= Logger.getLogger(EPMLog.LOG_NAME);

	private EPMLog() {
		super();
	}

	public static Logger getLog() {
		return log;
	}

}
