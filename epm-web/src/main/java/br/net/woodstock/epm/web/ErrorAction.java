package br.net.woodstock.epm.web;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ErrorAction extends AbstractAction {

	private static final long	serialVersionUID	= -3747486009547694131L;

	public ErrorAction() {
		super();
	}

	public String getMessage() {
		Exception e = this.getException();
		if (e != null) {
			return e.getMessage();
		}
		return null;
	}

	public String getStackTrace() {
		Exception e = this.getException();
		if (e != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			return stringWriter.getBuffer().toString();
		}
		return null;
	}

	private Exception getException() {
		RequestContext context = RequestContextHolder.getRequestContext();
		if (context != null) {
			Exception e = (Exception) context.getExternalContext().getSessionMap().get("exception");
			return e;
		}
		return null;
	}

}
