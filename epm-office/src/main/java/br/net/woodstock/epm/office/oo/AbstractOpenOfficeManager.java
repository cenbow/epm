package br.net.woodstock.epm.office.oo;

import br.net.woodstock.epm.office.OfficeException;

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

public class AbstractOpenOfficeManager {

	private static final String		UNO_URL	= "socket,host=%s,port=%d,tcpNoDelay=1";

	private XComponentContext		componentContext;

	private XMultiComponentFactory	multiComponentFactory;

	private Connector				connector;

	private Object					bridgeInstance;

	private XBridgeFactory			bridgeFactory;

	private XConnector				xConnector;

	private String					host;

	private int						port;

	private String					url;

	public AbstractOpenOfficeManager(final String host, final int port) {
		super();
		this.host = host;
		this.port = port;
		this.init();
	}

	private void init() {
		try {
			this.componentContext = Bootstrap.createInitialComponentContext(null);
			this.multiComponentFactory = this.componentContext.getServiceManager();
			this.connector = (Connector) this.multiComponentFactory.createInstanceWithContext("com.sun.star.connection.Connector", this.componentContext);
			this.bridgeInstance = this.componentContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.BridgeFactory", this.componentContext);
			this.bridgeFactory = UnoRuntime.queryInterface(XBridgeFactory.class, this.bridgeInstance);
			this.xConnector = UnoRuntime.queryInterface(XConnector.class, this.connector);
			this.url = String.format(UNO_URL, this.host, Integer.valueOf(this.port));
		} catch (Exception e) {
			throw new OfficeException(e);
		}
	}

	public <T> T execute(final OpenOfficeExecutor executor) {
		XConnection xConnection = null;
		try {
			xConnection = this.xConnector.connect(this.url);
			XBridge xbridge = this.bridgeFactory.createBridge("", "urp", xConnection, null);
			UnoRuntime.queryInterface(XComponent.class, xbridge);

			Object serviceManager = xbridge.getInstance("StarOffice.ServiceManager");

			XMultiComponentFactory multiComponentFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, serviceManager);

			XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, multiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", this.componentContext));

			XComponentLoader componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, xDesktop);
			T t = executor.doInConnection(componentLoader);
			return t;
		} catch (Exception e) {
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
