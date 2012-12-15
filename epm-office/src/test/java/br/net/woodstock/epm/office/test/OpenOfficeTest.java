package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.oo.io.OpenOfficeIO;
import br.net.woodstock.rockframework.utils.IOUtils;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.comp.connections.Connector;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextField;
import com.sun.star.text.XTextFieldsSupplier;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XRefreshable;
import com.sun.star.util.XUpdatable;

@RunWith(BlockJUnit4ClassRunner.class)
public class OpenOfficeTest {

	private static final String	UNO_URL	= "socket,host=localhost,port=8100,tcpNoDelay=1";

	public OpenOfficeTest() {
		super();
	}

	// @Test
	public void test1() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		IOUtils.toByteArray(inputStream);
		inputStream.close();

	}

	// @Test
	public void test2() throws Exception {
		XComponentContext componentContext = Bootstrap.createInitialComponentContext(null);
		XMultiComponentFactory multiComponentFactory = componentContext.getServiceManager();
		Connector connector = (Connector) multiComponentFactory.createInstanceWithContext("com.sun.star.connection.Connector", componentContext);
		Object bridgeInstance = componentContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.BridgeFactory", componentContext);
		XConnector xConnector = UnoRuntime.queryInterface(XConnector.class, connector);
		XConnection xConnection = xConnector.connect(OpenOfficeTest.UNO_URL);
		XBridgeFactory xbridgefactory = UnoRuntime.queryInterface(XBridgeFactory.class, bridgeInstance);
		XBridge xbridge = xbridgefactory.createBridge("", "urp", xConnection, null);
		UnoRuntime.queryInterface(XComponent.class, xbridge);

		Object serviceManager = xbridge.getInstance("StarOffice.ServiceManager");

		multiComponentFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, serviceManager);

		XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, multiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", componentContext));

		XComponentLoader componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, xDesktop);

		System.out.println(componentLoader);
	}

	// @Test
	public void test3() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		byte[] inDoc = IOUtils.toByteArray(inputStream);
		inputStream.close();

		File file = File.createTempFile("openoffice-test", "ott");
		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(inDoc);
		outputStream.close();

		XComponentContext componentContext = Bootstrap.createInitialComponentContext(null);
		XMultiComponentFactory multiComponentFactory = componentContext.getServiceManager();
		Connector connector = (Connector) multiComponentFactory.createInstanceWithContext("com.sun.star.connection.Connector", componentContext);
		Object bridgeInstance = componentContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.BridgeFactory", componentContext);
		XConnector xConnector = UnoRuntime.queryInterface(XConnector.class, connector);
		XConnection xConnection = xConnector.connect(OpenOfficeTest.UNO_URL);
		XBridgeFactory xbridgefactory = UnoRuntime.queryInterface(XBridgeFactory.class, bridgeInstance);
		XBridge xbridge = xbridgefactory.createBridge("", "urp", xConnection, null);
		UnoRuntime.queryInterface(XComponent.class, xbridge);

		Object serviceManager = xbridge.getInstance("StarOffice.ServiceManager");

		multiComponentFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, serviceManager);

		XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, multiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", componentContext));

		XComponentLoader componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, xDesktop);

		PropertyValue[] loadProps = new PropertyValue[1];
		loadProps[0] = new PropertyValue();
		loadProps[0].Name = "AsTemplate";
		loadProps[0].Value = new Boolean(true);

		// load
		XComponent component = componentLoader.loadComponentFromURL(file.toURI().toURL().toString(), "_blank", 0, loadProps);
		System.out.println(component);
	}

	@Test
	public void test4() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("teste.ott");
		
		File file = File.createTempFile("xxx", ".ott");
		FileOutputStream outputStream = new FileOutputStream(file);
		
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();

		XComponentContext componentContext = Bootstrap.createInitialComponentContext(null);
		XMultiComponentFactory multiComponentFactory = componentContext.getServiceManager();
		Connector connector = (Connector) multiComponentFactory.createInstanceWithContext("com.sun.star.connection.Connector", componentContext);
		Object bridgeInstance = componentContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.BridgeFactory", componentContext);
		XConnector xConnector = UnoRuntime.queryInterface(XConnector.class, connector);
		XConnection xConnection = xConnector.connect(OpenOfficeTest.UNO_URL);
		XBridgeFactory xbridgefactory = UnoRuntime.queryInterface(XBridgeFactory.class, bridgeInstance);
		XBridge xbridge = xbridgefactory.createBridge("", "urp", xConnection, null);

		Object serviceManager = xbridge.getInstance("StarOffice.ServiceManager");

		multiComponentFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, serviceManager);

		XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, multiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", componentContext));

		XComponentLoader componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, xDesktop);

		//PropertyValue[] loadProps = new PropertyValue[3];
		PropertyValue[] loadProps = new PropertyValue[2];
		loadProps[0] = new PropertyValue();
		loadProps[0].Name = "AsTemplate";
		loadProps[0].Value = Boolean.valueOf(true);
		
		loadProps[1] = new PropertyValue();
		loadProps[1].Name = "Hidden";
		loadProps[1].Value = Boolean.valueOf(true);

		/*loadProps[2] = new PropertyValue();
		loadProps[2].Name = "InputStream";
		loadProps[2].Value = OpenOfficeIO.toXInputStream(inputStream);*/

		

		inputStream.close();

		// load
		//XComponent component = componentLoader.loadComponentFromURL("private:stream", "_blank", 0, loadProps);
		XComponent component = componentLoader.loadComponentFromURL(file.toURI().toURL().toString(), "_blank", 0, loadProps);
		System.out.println(component);
		XModel model = UnoRuntime.queryInterface(XModel.class, component);

		if (model != null) {
			XTextDocument textDocument = UnoRuntime.queryInterface(XTextDocument.class, model);
			System.out.println("TExt: " + textDocument.getText().getString());
		}

		XTextFieldsSupplier xTextFieldsSupplier = UnoRuntime.queryInterface(XTextFieldsSupplier.class, component);

		// access the TextFields and the TextFieldMasters collections
		XNameAccess xNamedFieldMasters = xTextFieldsSupplier.getTextFieldMasters();
		XEnumerationAccess xEnumeratedFields = xTextFieldsSupplier.getTextFields();

		System.out.println("Campos");
		for (String s : xNamedFieldMasters.getElementNames()) {
			if (s.startsWith("com.sun.star.text.fieldmaster.User")) {
				System.out.println(s);
				Object fieldMaster = xNamedFieldMasters.getByName(s);
				XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class, fieldMaster);
				xPropertySet.setPropertyValue("Content", "Teste");

				XTextField[] fields = ((XTextField[]) xPropertySet.getPropertyValue("DependentTextFields"));

				for (int i = 0; i < fields.length; i++) {
					UnoRuntime.queryInterface(XUpdatable.class, fields[i]).update();
				}

			}
		}

		/*
		 * XEnumeration xEnumeration = xEnumeratedFields.createEnumeration(); System.out.println("Outros");
		 * while (xEnumeration.hasMoreElements()) { Any any = (Any) xEnumeration.nextElement(); XTextField
		 * textField = (XTextField) any.getObject(); System.out.println(textField.getPresentation(true)); }
		 */

		XRefreshable xRefreshable = UnoRuntime.queryInterface(XRefreshable.class, xEnumeratedFields);
		xRefreshable.refresh();

		outputStream = new FileOutputStream("/tmp/teste.odt");

		XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, component);
		PropertyValue[] storeProps = new PropertyValue[2];
		storeProps[0] = new PropertyValue();
		storeProps[0].Name = "FilterName";
		storeProps[0].Value = "writer8";
		storeProps[1] = new PropertyValue();
		storeProps[1].Name = "OutputStream";
		storeProps[1].Value = OpenOfficeIO.toXOutputStream(outputStream);
		xStorable.storeToURL("private:stream", storeProps);

		outputStream.close();

		xConnection.close();
		
	}

	// @Test
	public void test5() throws Exception {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("teste.docx");

		XComponentContext componentContext = Bootstrap.createInitialComponentContext(null);
		XMultiComponentFactory multiComponentFactory = componentContext.getServiceManager();
		Connector connector = (Connector) multiComponentFactory.createInstanceWithContext("com.sun.star.connection.Connector", componentContext);
		Object bridgeInstance = componentContext.getServiceManager().createInstanceWithContext("com.sun.star.bridge.BridgeFactory", componentContext);
		XConnector xConnector = UnoRuntime.queryInterface(XConnector.class, connector);
		XConnection xConnection = xConnector.connect(OpenOfficeTest.UNO_URL);
		XBridgeFactory xbridgefactory = UnoRuntime.queryInterface(XBridgeFactory.class, bridgeInstance);
		XBridge xbridge = xbridgefactory.createBridge("", "urp", xConnection, null);
		UnoRuntime.queryInterface(XComponent.class, xbridge);

		Object serviceManager = xbridge.getInstance("StarOffice.ServiceManager");

		multiComponentFactory = UnoRuntime.queryInterface(XMultiComponentFactory.class, serviceManager);

		XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, multiComponentFactory.createInstanceWithContext("com.sun.star.frame.Desktop", componentContext));

		XComponentLoader componentLoader = UnoRuntime.queryInterface(XComponentLoader.class, xDesktop);

		PropertyValue[] loadProps = new PropertyValue[2];
		loadProps[0] = new PropertyValue();
		loadProps[0].Name = "InputStream";
		loadProps[0].Value = OpenOfficeIO.toXInputStream(inputStream);

		loadProps[1] = new PropertyValue();
		loadProps[1].Name = "Hidden";
		loadProps[1].Value = Boolean.valueOf(true);

		inputStream.close();

		// load
		XComponent component = componentLoader.loadComponentFromURL("private:stream", "_blank", 0, loadProps);

		System.out.println(component);
		XModel model = UnoRuntime.queryInterface(XModel.class, component);

		if (model != null) {
			XTextDocument textDocument = UnoRuntime.queryInterface(XTextDocument.class, model);
			System.out.println("TExt: " + textDocument.getText().getString());
		}

		XTextFieldsSupplier xTextFieldsSupplier = UnoRuntime.queryInterface(XTextFieldsSupplier.class, component);

		// access the TextFields and the TextFieldMasters collections
		XNameAccess xNamedFieldMasters = xTextFieldsSupplier.getTextFieldMasters();
		XEnumerationAccess xEnumeratedFields = xTextFieldsSupplier.getTextFields();

		System.out.println("Campos");
		for (String s : xNamedFieldMasters.getElementNames()) {
			if (s.startsWith("com.sun.star.text.fieldmaster.User")) {
				System.out.println(s);
				Object fieldMaster = xNamedFieldMasters.getByName(s);
				XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class, fieldMaster);
				xPropertySet.setPropertyValue("Content", "Teste");

				XTextField[] fields = ((XTextField[]) xPropertySet.getPropertyValue("DependentTextFields"));

				for (int i = 0; i < fields.length; i++) {
					UnoRuntime.queryInterface(XUpdatable.class, fields[i]).update();
				}

			}
		}

		/*
		 * XEnumeration xEnumeration = xEnumeratedFields.createEnumeration(); System.out.println("Outros");
		 * while (xEnumeration.hasMoreElements()) { Any any = (Any) xEnumeration.nextElement(); XTextField
		 * textField = (XTextField) any.getObject(); System.out.println(textField.getPresentation(true)); }
		 */

		XRefreshable xRefreshable = UnoRuntime.queryInterface(XRefreshable.class, xEnumeratedFields);
		xRefreshable.refresh();

		FileOutputStream outputStream = new FileOutputStream("/tmp/teste2.odt");

		XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, component);
		PropertyValue[] storeProps = new PropertyValue[2];
		storeProps[0] = new PropertyValue();
		storeProps[0].Name = "FilterName";
		storeProps[0].Value = "writer8";
		storeProps[1] = new PropertyValue();
		storeProps[1].Name = "OutputStream";
		storeProps[1].Value = OpenOfficeIO.toXOutputStream(outputStream);
		xStorable.storeToURL("private:stream", storeProps);

		outputStream.close();
	}

}
