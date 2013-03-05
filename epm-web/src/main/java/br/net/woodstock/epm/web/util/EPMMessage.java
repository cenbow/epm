package br.net.woodstock.epm.web.util;

import java.io.Serializable;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class EPMMessage implements Serializable {

	private static final long	serialVersionUID	= 4586244468342338243L;

	@Autowired(required = true)
	private MessageSource		messageSource;

	public EPMMessage() {
		super();
	}

	public String getMessage(final String name, final Object[] args, final Locale locale) {
		return this.messageSource.getMessage(name, args, locale);
	}

}
