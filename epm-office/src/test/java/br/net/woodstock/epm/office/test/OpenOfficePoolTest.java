package br.net.woodstock.epm.office.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.epm.office.oo.OpenOfficeServer;
import br.net.woodstock.epm.office.oo.impl.ConversionExecutor;
import br.net.woodstock.epm.office.oo.impl.DefaultOpenOfficeManager;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficePool;
import br.net.woodstock.rockframework.utils.IOUtils;

public final class OpenOfficePoolTest {

	private OpenOfficePoolTest() {
		super();
	}

	public static void main(final String[] args) {
		try {
			InputStream input = OpenOfficeManagerMain.class.getClassLoader().getResourceAsStream("teste.ott");
			final byte[] bytes = IOUtils.toByteArray(input);

			final OpenOfficeServer server = new SocketOpenOfficePool(new int[] { 8100, 8101, 8102, 8103 });

			server.start();

			for (int j = 0; j < 50; j++) {
				OpenOfficeConnection connection = server.getConnection();

				OpenOfficeManager manager = new DefaultOpenOfficeManager(connection);

				long l = System.currentTimeMillis();
				ConversionExecutor template = new ConversionExecutor(new ByteArrayInputStream(bytes), OfficeDocumentType.ODT);
				InputStream output = manager.execute(template);

				File file = File.createTempFile("teste", ".docx");
				FileOutputStream outputStream = new FileOutputStream(file);
				IOUtils.copy(output, outputStream);

				output.close();
				outputStream.close();
				l = System.currentTimeMillis() - l;
				System.out.println("Resultado em " + l + "ms, arquivo " + file.getAbsolutePath());

			}

			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
