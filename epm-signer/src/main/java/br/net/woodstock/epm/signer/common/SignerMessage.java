package br.net.woodstock.epm.signer.common;

import br.net.woodstock.rockframework.config.AbstractMessage;

public abstract class SignerMessage {

	private static final String		BUNDLE_FILE	= "signer-messages";

	private static AbstractMessage	message;

	static {
		message = new AbstractMessage(SignerMessage.BUNDLE_FILE) {
			//
		};
	}

	public static String getMessage(final String key) {
		return SignerMessage.message.getMessage(key);
	}

	public static String getMessage(final String key, final Object... args) {
		return SignerMessage.message.getMessage(key, args);
	}

}
