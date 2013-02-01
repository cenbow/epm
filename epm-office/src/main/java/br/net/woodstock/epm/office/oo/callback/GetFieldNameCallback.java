package br.net.woodstock.epm.office.oo.callback;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeCallback;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeException;
import br.net.woodstock.epm.office.oo.impl.OpenOfficeHelper;

import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;

public class GetFieldNameCallback implements OpenOfficeCallback<Collection<String>> {

	private InputStream	source;

	public GetFieldNameCallback(final InputStream source) {
		super();
		this.source = source;
	}

	@Override
	public Collection<String> doInConnection(final OpenOfficeConnection connection) {
		try {
			XComponentLoader componentLoader = (XComponentLoader) connection.getDelegate();

			XComponent component = CallbackHelper.load(componentLoader, this.source, true);

			XTextFieldsSupplier xTextFieldsSupplier = UnoRuntime.queryInterface(XTextFieldsSupplier.class, component);

			XNameAccess xNamedFieldMasters = xTextFieldsSupplier.getTextFieldMasters();

			Collection<String> names = new HashSet<String>();
			for (String s : xNamedFieldMasters.getElementNames()) {
				if (OpenOfficeHelper.isUserField(s)) {
					Object fieldMaster = xNamedFieldMasters.getByName(s);
					if (fieldMaster != null) {
						names.add(OpenOfficeHelper.getUserFieldName(s));
					}
				}
			}

			CallbackHelper.close(component);

			return names;
		} catch (Exception e) {
			OfficeLog.getLogger().error(e.getMessage(), e);
			throw new OpenOfficeException(e);
		}
	}

}
