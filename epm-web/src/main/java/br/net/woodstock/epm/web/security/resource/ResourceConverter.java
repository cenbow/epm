package br.net.woodstock.epm.web.security.resource;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.net.woodstock.epm.orm.Resource;
import br.net.woodstock.epm.security.api.SecurityService;
import br.net.woodstock.rockframework.core.utils.Conditions;

@Component("resourceConverter")
public class ResourceConverter implements Converter, Serializable {

	private static final long	serialVersionUID	= 6721076267718630664L;

	@Autowired(required = true)
	private SecurityService		securityService;

	public ResourceConverter() {
		super();
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		if (Conditions.isNotEmpty(value)) {
			Integer id = Integer.valueOf(value);
			Resource resource = this.securityService.getResourceById(id);
			return resource;
		}
		return null;
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value != null) {
			Resource resource = (Resource) value;
			return resource.getId().toString();
		}
		return null;
	}

}
