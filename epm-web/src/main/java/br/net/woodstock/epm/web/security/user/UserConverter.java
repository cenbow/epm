package br.net.woodstock.epm.web.security.user;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.net.woodstock.epm.orm.User;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.rockframework.core.utils.Conditions;

@Component("userConverter")
public class UserConverter implements Converter, Serializable {

	private static final long	serialVersionUID	= -5158458545727079346L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public UserConverter() {
		super();
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		if (Conditions.isNotEmpty(value)) {
			Integer id = Integer.valueOf(value);
			User user = this.securityService.getUserById(id);
			return user;
		}
		return null;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value != null) {
			User user = (User) value;
			return user.getId().toString();
		}
		return null;
	}

}
