package br.net.woodstock.epm.office.oo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.OfficeException;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.UnoRuntime;

public class ConversionExecutor implements OpenOfficeExecutor {

	private InputStream			source;

	private OfficeDocumentType	targetType;

	public ConversionExecutor(final InputStream source, final OfficeDocumentType targetType) {
		super();
		this.source = source;
		this.targetType = targetType;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T doInConnection(final XComponentLoader componentLoader) {
		try {
			PropertyValue[] loadProps = new PropertyValue[2];
			loadProps[0] = new PropertyValue();
			loadProps[0].Name = OpenOfficeHelper.INPUT_STREAM_PROPERTY;
			loadProps[0].Value = OpenOfficeIO.toXInputStream(this.source);

			loadProps[1] = new PropertyValue();
			loadProps[1].Name = OpenOfficeHelper.HIDDEN_PROPERTY;
			loadProps[1].Value = Boolean.valueOf(true);

			XComponent component = componentLoader.loadComponentFromURL(OpenOfficeHelper.PRIVATE_STREAM_URL, OpenOfficeHelper.BLANK_TARGET, 0, loadProps);
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
