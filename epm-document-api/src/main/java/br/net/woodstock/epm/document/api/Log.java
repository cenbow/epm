package br.net.woodstock.epm.document.api;

import java.util.logging.Logger;

public abstract class Log {

	private static final Logger	LOGGER	= Logger.getLogger("br.net.woodstock.epm.document");

	private Log() {
		//
	}

	public static Logger getLogger() {
		return Log.LOGGER;
	}

}
