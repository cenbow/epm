package br.net.woodstock.epm.office.oo.impl;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import br.net.woodstock.epm.office.OfficeException;
import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeExecutor;

import com.sun.star.beans.PropertyValue;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;

public class GetFieldNameExecutor implements OpenOfficeExecutor {

	private InputStream	source;

	public GetFieldNameExecutor(final InputStream source) {
		super();
		this.source = source;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T doInConnection(final OpenOfficeConnection connection) {
		try {
			XComponentLoader componentLoader = (XComponentLoader) connection.getDelegate();
			PropertyValue[] loadProps = new PropertyValue[3];
			loadProps[0] = new PropertyValue();
			loadProps[0].Name = OpenOfficeHelper.AS_TEMPLATE_PROPERTY;
			loadProps[0].Value = Boolean.valueOf(true);

			loadProps[1] = new PropertyValue();
			loadProps[1].Name = OpenOfficeHelper.INPUT_STREAM_PROPERTY;
			loadProps[1].Value = OpenOfficeIO.toXInputStream(this.source);

			loadProps[2] = new PropertyValue();
			loadProps[2].Name = OpenOfficeHelper.HIDDEN_PROPERTY;
			loadProps[2].Value = Boolean.valueOf(true);

			XComponent component = componentLoader.loadComponentFromURL(OpenOfficeHelper.PRIVATE_STREAM_URL, OpenOfficeHelper.BLANK_TARGET, 0, loadProps);

			XTextFieldsSupplier xTextFieldsSupplier = UnoRuntime.queryInterface(XTextFieldsSupplier.class, component);

			XNameAccess xNamedFieldMasters = xTextFieldsSupplier.getTextFieldMasters();

			Set<String> names = new HashSet<String>();
			for (String s : xNamedFieldMasters.getElementNames()) {
				if (OpenOfficeHelper.isUserField(s)) {
					Object fieldMaster = xNamedFieldMasters.getByName(s);
					if (fieldMaster != null) {
						names.add(OpenOfficeHelper.getUserFieldName(s));
					}
				}
			}
			return (T) names;
		} catch (Exception e) {
			OfficeLog.getLogger().error(e.getMessage(), e);
			throw new OfficeException(e);
		}
	}

}
