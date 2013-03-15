package br.net.woodstock.epm.util.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.net.woodstock.epm.util.EPMMessage;
import br.net.woodstock.rockframework.core.util.MessageBundle;

@Component
public class EPMMessageImpl implements EPMMessage {

	private static final long	serialVersionUID	= 930859803170924427L;

	@Autowired(required = true)
	private MessageBundle		messageBundle;

	public EPMMessageImpl() {
		super();
	}

	@Override
	public String getMessage(final String name, final Object[] args, final Locale locale) {
		return this.messageBundle.getMessage(name, locale, args);
	}

}
