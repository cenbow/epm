package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.impl.AbstractOpenOfficeConnection;
import br.net.woodstock.epm.office.oo.impl.AbstractOpenOfficeServer;
import br.net.woodstock.epm.office.oo.impl.ConversionExecutor;
import br.net.woodstock.epm.office.oo.impl.PipeOpenOfficeConnection;
import br.net.woodstock.epm.office.oo.impl.PipeOpenOfficeServer;
import br.net.woodstock.epm.office.oo.impl.DefaultOpenOfficeManager;
import br.net.woodstock.rockframework.utils.IOUtils;

public final class PipeOpenOfficeServerTestMain {

	private PipeOpenOfficeServerTestMain() {
		super();
	}

	public static void main(final String[] args) {
		AbstractOpenOfficeServer server = null;
		try {
			server = new PipeOpenOfficeServer("ooinstance1");
			server.start();

			InputStream input = PipeOpenOfficeServerTestMain.class.getClassLoader().getResourceAsStream("teste.ott");
			AbstractOpenOfficeConnection connection = new PipeOpenOfficeConnection("ooinstance1");
			DefaultOpenOfficeManager manager = new DefaultOpenOfficeManager(connection);
			ConversionExecutor template = new ConversionExecutor(input, OfficeDocumentType.DOCX);
			InputStream output = manager.execute(template);

			File file = File.createTempFile("teste", ".docx");
			FileOutputStream outputStream = new FileOutputStream(file);
			IOUtils.copy(output, outputStream);

			input.close();
			output.close();
			outputStream.close();
			System.out.println("File: " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				server.stop();
			}
		}
	}
}
