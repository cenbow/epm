package br.net.woodstock.epm.signer.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SignerLog {

	private static final String	NAME	= "br.net.woodstock.epm.signer";

	private static final Logger	LOGGER	= LoggerFactory.getLogger(SignerLog.NAME);

	private SignerLog() {
		super();
	}

	public static Logger getLogger() {
		return SignerLog.LOGGER;
	}

}
