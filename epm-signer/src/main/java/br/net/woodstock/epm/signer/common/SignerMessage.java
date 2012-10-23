package br.net.woodstock.epm.signer.common;

import br.net.woodstock.rockframework.config.AbstractMessage;

public final class SignerMessage extends AbstractMessage {

	private static final String		BUNDLE_FILE	= "signer-messages";

	private static SignerMessage	instance	= new SignerMessage();

	private SignerMessage() {
		super(SignerMessage.BUNDLE_FILE);
	}

	public static SignerMessage getInstance() {
		return SignerMessage.instance;
	}

}
