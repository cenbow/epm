package br.net.woodstock.epm.web.security.role;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.net.woodstock.epm.orm.Role;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.rockframework.utils.ConditionUtils;

@Component("roleConverter")
public class RoleConverter implements Converter, Serializable {

	private static final long	serialVersionUID	= 2024273919561277956L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public RoleConverter() {
		super();
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		if (ConditionUtils.isNotEmpty(value)) {
			Integer id = Integer.valueOf(value);
			Role role = this.securityService.getRoleById(id);
			return role;
		}
		return null;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value != null) {
			Role role = (Role) value;
			return role.getId().toString();
		}
		return null;
	}

}
