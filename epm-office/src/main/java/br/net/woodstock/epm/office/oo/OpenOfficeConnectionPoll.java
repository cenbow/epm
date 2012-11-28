package br.net.woodstock.epm.office.oo;

import br.net.woodstock.epm.office.OfficeException;
import br.net.woodstock.epm.util.EPMLog;

import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.comp.connections.Connector;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

public class OpenOfficeConnectionPoll {

	private static final String	UNO_URL	= "socket,host=%s,port=%d,tcpNoDelay=1";

	private String				host;

	private int					port;

	private String				url;

	public OpenOfficeConnectionPoll(final String host, final int port) {
		super();
		this.host = host;
		this.port = port;
		this.url = String.format(UNO_URL, this.host, Integer.valueOf(this.port));
	}

	public <T> T execute(final OpenOfficeExecutor executor) {
		XComponentContext componentContext = null;
		XMultiComponentFactory multiComponentFactory = null;
		Connector connector = null;
		Object bridgeInstance = null;
		XBridgeFactory bridgeFactory = null;
		XConnector xConnector = null;
		XConnection xConnection = null;
		XDesktop xDesktop = null;
		try {
			componentContext = Bootstrap.createInitialComponentContext(null);
			multiComponentFactory = componentContext.getServiceManager();
			connector = (Connector) multiComponentFactory.createInstanceWithContext("com.sun.star.connection.Connector", componentContext);
			bridgeInstance = componentContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.BridgeFactory", componentContext);
			bridgeFactory = UnoRuntime.queryInterface(XBridgeFactory.class, bridgeInstance);
			xConnector = UnoRuntime.queryInterface(XConnector.class, connector);
			xConnection = xConnector.connect(this.url);
			XBridge xbridge = bridgeFactory.createBridge("", "urp", xConnection, null);
			UnoRuntime.queryInterface(XComponent.class, xbridge);

			Object serviceManager = xbridge.getInstance("StarOffice.ServiceManager");

			XMultiComponentFactory localMultiComponentFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, serviceManager);

			xDesktop = UnoRuntime.queryInterface(XDesktop.class, localMultiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", componentContext));

			XComponentLoader componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, xDesktop);
			T t = executor.doInConnection(componentLoader);
			return t;
		} catch (Exception e) {
			EPMLog.getLogger().error(e.getMessage(), e);
			throw new OfficeException(e);
		} finally {
			if (xConnection != null) {
				try {
					xConnection.close();
				} catch (IOException e) {
					//
				}
			}
		}
	}
}
