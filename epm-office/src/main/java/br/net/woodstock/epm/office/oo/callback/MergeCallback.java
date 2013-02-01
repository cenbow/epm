package br.net.woodstock.epm.office.oo.callback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.style.BreakType;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.text.XTextViewCursorSupplier;
import com.sun.star.uno.UnoRuntime;

public class MergeCallback implements OpenOfficeCallback<InputStream> {

	private InputStream[]		sources;

	private OfficeDocumentType	targetType;

	public MergeCallback(final InputStream[] sources, final OfficeDocumentType targetType) {
		super();
		this.sources = sources;
		this.targetType = targetType;
	}

	@Override
	public InputStream doInConnection(final OpenOfficeConnection connection) {
		try {
			XComponentLoader componentLoader = (XComponentLoader) connection.getDelegate();

			XComponent component = CallbackHelper.load(componentLoader, this.sources[0], false);

			String currentFilterName = this.getFilterName(component);

			FilterMappingResolver filterMappingResolver = FilterMappingResolver.getInstance();
			FilterMapping filterMapping = filterMappingResolver.getFilterMapping(currentFilterName, this.targetType);

			if (filterMapping == null) {
				throw new OpenOfficeException("Cannot convert to " + this.targetType);
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			XTextDocument document = UnoRuntime.queryInterface(XTextDocument.class, component);
			XController controller = document.getCurrentController();
			XTextViewCursorSupplier textViewCursorSupplier = UnoRuntime.queryInterface(XTextViewCursorSupplier.class, controller);
			XTextViewCursor textViewCursor = textViewCursorSupplier.getViewCursor();
			XTextCursor textCursor = textViewCursor.getText().createTextCursor();

			for (int i = 1; i < this.sources.length; i++) {
				textCursor.gotoEnd(false);
				document.getText().insertControlCharacter(textCursor, ControlCharacter.PARAGRAPH_BREAK, false);

				XPropertySet propertySet = UnoRuntime.queryInterface(XPropertySet.class, textCursor);
				propertySet.setPropertyValue(OpenOfficeHelper.PAGE_BREAK_PROPERTY, BreakType.PAGE_BEFORE);

				XDocumentInsertable insertable = UnoRuntime.queryInterface(XDocumentInsertable.class, textCursor);
				String url = CallbackHelper.createTempFile(this.sources[i]);
				insertable.insertDocumentFromURL(url, new PropertyValue[0]);
			}

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

			return new ByteArrayInputStream(outputStream.toByteArray());
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
