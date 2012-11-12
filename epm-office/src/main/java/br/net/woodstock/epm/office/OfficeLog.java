package br.net.woodstock.epm.office;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OfficeLog {

	private static final String	NAME	= "br.net.woodstock.epm.office";

	private static final Logger	LOGGER	= LoggerFactory.getLogger(OfficeLog.NAME);

	private OfficeLog() {
		super();
	}

	public static Logger getLogger() {
		return OfficeLog.LOGGER;
	}

}
