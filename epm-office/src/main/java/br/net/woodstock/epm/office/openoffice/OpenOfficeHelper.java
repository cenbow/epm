package br.net.woodstock.epm.office.openoffice;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import br.net.woodstock.epm.office.OfficeDocument;
import br.net.woodstock.epm.office.OfficeException;

import com.sun.star.awt.XWindow;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.comp.bridgefactory.BridgeFactory;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.Connector;
import com.sun.star.connection.NoConnectException;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDispatchHelper;
import com.sun.star.frame.XDispatchProvider;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lib.uno.adapter.ByteArrayToXInputStreamAdapter;
import com.sun.star.lib.uno.adapter.OutputStreamToXOutputStreamAdapter;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

abstract class OpenOfficeHelper {

	public static OfficeDocument openDocument(final URL url, final byte[] bytes) {
		PropertyValue[] properties = new PropertyValue[2];
		properties[0] = new PropertyValue();
		properties[0].Name = "InputStream";
		properties[0].Value = new ByteArrayToXInputStreamAdapter(bytes);
		properties[1] = new PropertyValue();
		properties[1].Name = "Hidden";
		properties[1].Value = Boolean.TRUE;
		return OpenOfficeHelper.openDocumentInternal(url, "private:stream", properties);
	}

	private static OfficeDocument openDocumentInternal(final URL url, final String uno, final PropertyValue[] properties) {
		try {
			XComponentContext localContext = Bootstrap.createInitialComponentContext(null);
			XMultiComponentFactory localServiceManager = localContext.getServiceManager();
			XConnector connector = UnoRuntime.queryInterface(XConnector.class, localServiceManager.createInstanceWithContext(Connector.class.getCanonicalName(), localContext));
			XConnection connection = connector.connect(url.toString());
			XBridgeFactory bridgeFactory = UnoRuntime.queryInterface(XBridgeFactory.class, localServiceManager.createInstanceWithContext(BridgeFactory.class.getCanonicalName(), localContext));
			XBridge bridge = bridgeFactory.createBridge("", "urp", connection, null);
			XComponent bridgeComponent = UnoRuntime.queryInterface(XComponent.class, bridge);
			XMultiComponentFactory serviceManager = UnoRuntime.queryInterface(XMultiComponentFactory.class, bridge.getInstance("StarOffice.ServiceManager"));
			XPropertySet propertySet = UnoRuntime.queryInterface(XPropertySet.class, serviceManager);

			XComponentContext componentContext = UnoRuntime.queryInterface(XComponentContext.class, propertySet.getPropertyValue("DefaultContext"));
			XComponentLoader componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, serviceManager.createInstanceWithContext("com.sun.star.frame.Desktop", componentContext));
			XComponent document = componentLoader.loadComponentFromURL(uno, "_blank", 0, properties);

			if (document != null) {
				XModel xModel = UnoRuntime.queryInterface(XModel.class, document);
				XFrame xFrame = xModel.getCurrentController().getFrame();
				XWindow window = xFrame.getComponentWindow();
				Object dispatchHelperObject = componentContext.getServiceManager().createInstanceWithContext("com.sun.star.frame.DispatchHelper", componentContext);
				XDispatchHelper dispatchHelper = UnoRuntime.queryInterface(XDispatchHelper.class, dispatchHelperObject);
				XDispatchProvider dispatchProvider = UnoRuntime.queryInterface(XDispatchProvider.class, xFrame);
				XStorable storable = UnoRuntime.queryInterface(XStorable.class, document);
			}

			return null;
		} catch (Exception e) {
			throw new OfficeException(e);
		}
	}

	public void salvarDocumento(OutputStream os, TipoTexto tipoTexto) throws OfficeException {
		PropertyValue[] propsStore = new PropertyValue[2];
		propsStore[0] = new PropertyValue();
		propsStore[0].Name = "OutputStream";
		propsStore[0].Value = new OutputStreamToXOutputStreamAdapter(os);
		propsStore[1] = new PropertyValue();
		propsStore[1].Name = "FilterName";
		propsStore[1].Value = tipoTexto.getFiltro();
		try {
			storable.storeToURL("private:stream", propsStore);
		} catch (IOException e) {
			throw new OfficeException("erro ao salvar documento", e);
		}

	}

}
