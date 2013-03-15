package br.net.woodstock.epm.util;

import java.io.Serializable;
import java.util.Locale;

public interface EPMMessage extends Serializable {

	String getMessage(final String name, final Object[] args, final Locale locale);

}
