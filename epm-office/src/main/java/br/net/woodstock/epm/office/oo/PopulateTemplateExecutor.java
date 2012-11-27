package br.net.woodstock.epm.office.oo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.OfficeException;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextField;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XUpdatable;

public class PopulateTemplateExecutor implements OpenOfficeExecutor {

	private InputStream			source;

	private Map<String, String>	values;

	private OfficeDocumentType	targetType;

	public PopulateTemplateExecutor(final InputStream source, final Map<String, String> values, final OfficeDocumentType targetType) {
		super();
		this.source = source;
		this.values = values;
		this.targetType = targetType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T doInConnection(final XComponentLoader componentLoader) {
		try {
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

			String filterName = OpenOfficeHelper.getFilter(null, this.targetType);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, component);
			PropertyValue[] storeProps = new PropertyValue[2];
			storeProps[0] = new PropertyValue();
			storeProps[0].Name = OpenOfficeHelper.FILTER_NAME_PROPERTY;
			storeProps[0].Value = filterName;
			storeProps[1] = new PropertyValue();
			storeProps[1].Name = OpenOfficeHelper.OUTPUT_STREAM_PROPERTY;
			storeProps[1].Value = OpenOfficeIO.toXOutputStream(outputStream);
			xStorable.storeToURL(OpenOfficeHelper.PRIVATE_STREAM_URL, storeProps);
			return (T) new ByteArrayInputStream(outputStream.toByteArray());
		} catch (Exception e) {
			throw new OfficeException(e);
		}
	}

}
