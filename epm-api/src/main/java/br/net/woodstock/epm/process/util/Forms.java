package br.net.woodstock.epm.process.util;

import br.net.woodstock.epm.process.api.Form;
import br.net.woodstock.epm.process.api.FormField;

public abstract class Forms {

	private Forms() {
		//
	}

	public static FormField getField(final Form form, final String id) {
		if (form == null) {
			return null;
		}
		for (FormField field : form.getFields()) {
			if (field.getId().equals(id)) {
				return field;
			}
		}
		return null;
	}

}
