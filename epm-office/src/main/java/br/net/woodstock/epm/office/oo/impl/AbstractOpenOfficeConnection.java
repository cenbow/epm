package br.net.woodstock.epm.office.oo.impl;

import br.net.woodstock.epm.office.OfficeException;
import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;

import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.comp.connections.Connector;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

public abstract class AbstractOpenOfficeConnection implements OpenOfficeConnection {

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

	private boolean					connected;

	public AbstractOpenOfficeConnection() {
		super();
	}

	public abstract String getConnectionURL();

	@Override
	public Object getDelegate() {
		return this.componentLoader;
	}

	@Override
	public boolean isConnected() {
		return this.connected;
	}

	@Override
	public void connect() {
		try {
			this.componentContext = Bootstrap.createInitialComponentContext(null);
			this.multiComponentFactory = this.componentContext.getServiceManager();
			this.connector = (Connector) this.multiComponentFactory.createInstanceWithContext("com.sun.star.connection.Connector", this.componentContext);
			this.bridgeInstance = this.componentContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.BridgeFactory", this.componentContext);
			this.bridgeFactory = UnoRuntime.queryInterface(XBridgeFactory.class, this.bridgeInstance);
			this.xConnector = UnoRuntime.queryInterface(XConnector.class, this.connector);
			this.xConnection = this.xConnector.connect(this.getConnectionURL());
			this.xbridge = this.bridgeFactory.createBridge("", "urp", this.xConnection, null);
			this.xComponent = UnoRuntime.queryInterface(XComponent.class, this.xbridge);

			Object serviceManager = this.xbridge.getInstance("StarOffice.ServiceManager");

			this.localMultiComponentFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, serviceManager);

			this.xDesktop = UnoRuntime.queryInterface(XDesktop.class, this.localMultiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", this.componentContext));

			this.componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, this.xDesktop);
			this.connected = true;
		} catch (Exception e) {
			OfficeLog.getLogger().error(e.getMessage(), e);
			throw new OfficeException(e);
		}
	}

	@Override
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
		this.connected = false;
	}
}
