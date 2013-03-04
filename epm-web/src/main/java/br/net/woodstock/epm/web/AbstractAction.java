package br.net.woodstock.epm.web;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import br.net.woodstock.rockframework.web.faces.utils.FacesUtils;

public abstract class AbstractAction implements Serializable {

	private static final long	serialVersionUID	= 607647148450573867L;

	private static final String	MSG_OK				= "msg.ok";

	@Autowired(required = true)
	private MessageSource		messageSource;

	public String getMessageOK() {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		return this.messageSource.getMessage(AbstractAction.MSG_OK, null, locale);
	}

	public String getMessage(final String name, final Object[] args) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		return this.messageSource.getMessage(name, args, locale);
	}

	public void addFacesMessage(final String message) {
		FacesUtils.addMessage(message);
	}

}
