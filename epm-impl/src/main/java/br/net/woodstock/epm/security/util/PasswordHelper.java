package br.net.woodstock.epm.security.util;

import br.net.woodstock.rockframework.security.digest.PasswordEncoder;
import br.net.woodstock.rockframework.security.digest.impl.SHA1PasswordEncoder;

public abstract class PasswordHelper {

	private static final PasswordEncoder	ENCODER	= new SHA1PasswordEncoder();

	private PasswordHelper() {
		// super();
	}

	public static String encode(final String password) {
		return PasswordHelper.ENCODER.encode(password);
	}

}
