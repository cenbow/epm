package br.net.woodstock.epm.office.oo;

import br.net.woodstock.epm.office.OfficeException;

public class OpenOfficeException extends OfficeException {

	private static final long	serialVersionUID	= 916434198028546334L;

	public OpenOfficeException(final String message) {
		super(message);
	}

	public OpenOfficeException(final Throwable cause) {
		super(cause);
	}

	public OpenOfficeException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
