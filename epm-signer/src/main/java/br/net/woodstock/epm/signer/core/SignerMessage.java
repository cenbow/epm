package br.net.woodstock.epm.signer.core;

import br.net.woodstock.rockframework.core.util.MessageBundle;

public abstract class SignerMessage {

	private static final String		BUNDLE_FILE	= "signer-messages";

	private static MessageBundle	message		= new MessageBundle(SignerMessage.BUNDLE_FILE);

	public static String getMessage(final String key) {
		return SignerMessage.message.getMessage(key);
	}

	public static String getMessage(final String key, final Object... args) {
		return SignerMessage.message.getMessage(key, args);
	}

}
