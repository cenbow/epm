package br.net.woodstock.epm.office.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.AbstractOpenOfficeConnection;
import br.net.woodstock.epm.office.oo.AbstractOpenOfficeServer;
import br.net.woodstock.epm.office.oo.ConversionExecutor;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.epm.office.oo.PipeOpenOfficeConnection;
import br.net.woodstock.epm.office.oo.PipeOpenOfficeServer;
import br.net.woodstock.rockframework.utils.IOUtils;

public class PipeOpenOfficeServerTestMain {

	public PipeOpenOfficeServerTestMain() {
		super();
	}

	public static void main(String[] args) {
		AbstractOpenOfficeServer server = null;
		try {
			server = new PipeOpenOfficeServer("ooinstance1");
			server.start();

			InputStream input = PipeOpenOfficeServerTestMain.class.getClassLoader().getResourceAsStream("teste.ott");
			AbstractOpenOfficeConnection connection = new PipeOpenOfficeConnection("ooinstance1");
			OpenOfficeManager manager = new OpenOfficeManager(connection);
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
