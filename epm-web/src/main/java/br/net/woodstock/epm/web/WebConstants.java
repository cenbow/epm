package br.net.woodstock.epm.web;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WebConstants {

	public static final int	PAGE_SIZE	= 10;

	public WebConstants() {
		super();
	}

	public int getPageSize() {
		return PAGE_SIZE;
	}

}
