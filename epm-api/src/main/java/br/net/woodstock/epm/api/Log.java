package br.net.woodstock.epm.api;

import org.apache.log4j.Logger;

public abstract class Log {

	private static final Logger	LOGGER	= Logger.getLogger("br.net.woodstock.epm");

	public static Logger getLogger() {
		return Log.LOGGER;
	}

}
