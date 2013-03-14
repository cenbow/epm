package br.net.woodstock.epm.web.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

public abstract class EPMMessage {

	private static MessageSource	messageSource;

	private EPMMessage() {
		super();
	}

	public static void setMessageSource(final MessageSource messageSource) {
		EPMMessage.messageSource = messageSource;
	}

	public static String getMessage(final String name, final Object[] args, final Locale locale) {
		return EPMMessage.messageSource.getMessage(name, args, locale);
	}

}
