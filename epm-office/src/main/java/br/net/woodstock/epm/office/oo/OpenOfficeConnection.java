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
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XEventListener;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

public class OpenOfficeConnection implements XEventListener {

	private static final String		UNO_URL	= "socket,host=%s,port=%d,tcpNoDelay=1";

	private XComponentContext		componentContext;

	private XMultiComponentFactory	multiComponentFactory;

	private Connector				connector;

	private Object					bridgeInstance;

	private XBridgeFactory			bridgeFactory;

	private XConnector				xConnector;

	private XConnection				xConnection;

	private XBridge					xbridge;

	private XComponent				xComponent;

	private XMultiComponentFactory	localMultiComponentFactory;

	private XDesktop				xDesktop;

	private XComponentLoader		componentLoader;

	private String					url;

	public OpenOfficeConnection(final String host, final int port) {
		super();
		this.url = String.format(OpenOfficeConnection.UNO_URL, host, Integer.valueOf(port));
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
			this.xConnection = this.xConnector.connect(this.url);
			this.xbridge = this.bridgeFactory.createBridge("", "urp", this.xConnection, null);
			this.xComponent = UnoRuntime.queryInterface(XComponent.class, this.xbridge);

			Object serviceManager = this.xbridge.getInstance("StarOffice.ServiceManager");

			this.localMultiComponentFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, serviceManager);

			this.xDesktop = UnoRuntime.queryInterface(XDesktop.class, this.localMultiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", this.componentContext));

			this.componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, this.xDesktop);
		} catch (Exception e) {
			EPMLog.getLogger().error(e.getMessage(), e);
			throw new OfficeException(e);
		}
	}

	public XComponentLoader getComponentLoader() {
		return this.componentLoader;
	}

	@Override
	public void disposing(EventObject event) {
		//
	}

	public void close() {
		if (this.xComponent != null) {
			try {
				this.xComponent.dispose();
			} catch (Exception e) {
				//
			}
		}
		if (this.xConnection != null) {
			try {
				this.xConnection.close();
			} catch (Exception e) {
				//
			}
		}
	}
}
