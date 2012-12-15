package br.net.woodstock.epm.office.oo.callback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.FilterMapping;
import br.net.woodstock.epm.office.oo.OpenOfficeCallback;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeException;
import br.net.woodstock.epm.office.oo.impl.OpenOfficeHelper;
import br.net.woodstock.epm.office.oo.io.OpenOfficeIO;
import br.net.woodstock.epm.office.oo.mapping.FilterMappingResolver;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextField;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XUpdatable;

public class PopulateTemplateCallback implements OpenOfficeCallback {

	private InputStream			source;

	private Map<String, String>	values;

	private OfficeDocumentType	targetType;

	public PopulateTemplateCallback(final InputStream source, final Map<String, String> values, final OfficeDocumentType targetType) {
		super();
		this.source = source;
		this.values = values;
		this.targetType = targetType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T doInConnection(final OpenOfficeConnection connection) {
		try {
			XComponentLoader componentLoader = (XComponentLoader) connection.getDelegate();

			XComponent component = CallbackHelper.load(componentLoader, this.source, true);

			String currentFilterName = this.getFilterName(component);

			FilterMappingResolver filterMappingResolver = FilterMappingResolver.getInstance();
			FilterMapping filterMapping = filterMappingResolver.getFilterMapping(currentFilterName, this.targetType);

			if (filterMapping == null) {
				throw new OpenOfficeException("Cannot convert to " + this.targetType);
			}

			XTextFieldsSupplier xTextFieldsSupplier = UnoRuntime.queryInterface(XTextFieldsSupplier.class, component);

			XNameAccess xNamedFieldMasters = xTextFieldsSupplier.getTextFieldMasters();
			for (String s : xNamedFieldMasters.getElementNames()) {
				if (OpenOfficeHelper.isUserField(s)) {
					String name = OpenOfficeHelper.getUserFieldName(s);
					if (this.values.containsKey(name)) {
						Object fieldMaster = xNamedFieldMasters.getByName(s);
						XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class, fieldMaster);
						xPropertySet.setPropertyValue(OpenOfficeHelper.CONTENT_PROPERTY, this.values.get(name));

						XTextField[] fields = ((XTextField[]) xPropertySet.getPropertyValue(OpenOfficeHelper.DEPENDENT_TEXT_FIELD_PROPERTY));

						for (int i = 0; i < fields.length; i++) {
							UnoRuntime.queryInterface(XUpdatable.class, fields[i]).update();
						}
					}
				}
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, component);
			PropertyValue[] storeProps = new PropertyValue[2];
			storeProps[0] = new PropertyValue();
			storeProps[0].Name = OpenOfficeHelper.FILTER_NAME_PROPERTY;
			storeProps[0].Value = filterMapping.getExportFilter(this.targetType);
			storeProps[1] = new PropertyValue();
			storeProps[1].Name = OpenOfficeHelper.OUTPUT_STREAM_PROPERTY;
			storeProps[1].Value = OpenOfficeIO.toXOutputStream(outputStream);
			xStorable.storeToURL(OpenOfficeHelper.PRIVATE_STREAM_URL, storeProps);
			
			CallbackHelper.close(component);
			
			return (T) new ByteArrayInputStream(outputStream.toByteArray());
		} catch (Exception e) {
			OfficeLog.getLogger().error(e.getMessage(), e);
			throw new OpenOfficeException(e);
		}
	}

	private String getFilterName(final XComponent component) {
		XModel model = UnoRuntime.queryInterface(XModel.class, component);
		String filterName = null;
		PropertyValue[] args = model.getArgs();
		for (PropertyValue arg : args) {
			if (arg.Name.equals(OpenOfficeHelper.FILTER_NAME_PROPERTY)) {
				filterName = (String) arg.Value;
				break;
			}
		}
		return filterName;
	}

}
