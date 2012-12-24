package br.net.woodstock.epm.office.oo.callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.impl.OpenOfficeHelper;
import br.net.woodstock.rockframework.utils.IOUtils;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;

abstract class CallbackHelper {

	private static final String	PROTOCOL_SEPARATOR	= ":";

	private static final String	PATH_SEPARATOR		= "//";

	private CallbackHelper() {
		//
	}

	public static XComponent load(final XComponentLoader loader, final InputStream inputStream, final boolean template) throws java.io.IOException, IOException, IllegalArgumentException {
		PropertyValue[] loadProps = new PropertyValue[template ? 2 : 1];
		// loadProps[0] = new PropertyValue();
		// loadProps[0].Name = OpenOfficeHelper.INPUT_STREAM_PROPERTY;
		// loadProps[0].Value = OpenOfficeIO.toXInputStream(inputStream);

		loadProps[0] = new PropertyValue();
		loadProps[0].Name = OpenOfficeHelper.HIDDEN_PROPERTY;
		loadProps[0].Value = Boolean.TRUE;

		if (template) {
			loadProps[1] = new PropertyValue();
			loadProps[1].Name = OpenOfficeHelper.AS_TEMPLATE_PROPERTY;
			loadProps[1].Value = Boolean.TRUE;
		}

		File file = File.createTempFile("tmp", ".tmp");
		FileOutputStream outputStream = new FileOutputStream(file);

		IOUtils.copy(inputStream, outputStream);
		outputStream.close();

		String url = CallbackHelper.getFileURL(file);

		long l = System.currentTimeMillis();

		OfficeLog.getLogger().info("Loading component");
		XComponent component = loader.loadComponentFromURL(url, OpenOfficeHelper.BLANK_TARGET, 0, loadProps);
		OfficeLog.getLogger().info("Component loaded in " + (System.currentTimeMillis() - l) + "ms");

		file.delete();

		return component;
	}

	public static void close(final XComponent component) {
		XCloseable xCloseable = UnoRuntime.queryInterface(XCloseable.class, component);

		if (xCloseable != null) {
			try {
				xCloseable.close(false);
			} catch (CloseVetoException ex) {
				XComponent xComp = UnoRuntime.queryInterface(XComponent.class, component);
				xComp.dispose();
			}
		} else {
			component.dispose();
		}
	}

	private static String getFileURL(final File file) throws MalformedURLException {
		URL url = file.toURI().toURL();
		StringBuilder builder = new StringBuilder();
		builder.append(url.getProtocol());
		builder.append(CallbackHelper.PROTOCOL_SEPARATOR);
		builder.append(CallbackHelper.PATH_SEPARATOR);
		builder.append(url.getPath());
		return builder.toString();
	}

}
