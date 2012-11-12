package br.net.woodstock.epm.office;

import br.net.woodstock.rockframework.DelegateException;

public class OfficeException extends DelegateException {

	private static final long	serialVersionUID	= -6958778504120548911L;

	public OfficeException(final String message) {
		super(message);
	}

	public OfficeException(final Throwable cause) {
		super(cause);
	}

	public OfficeException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
