package br.net.woodstock.epm.office.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.epm.office.oo.impl.ConversionExecutor;
import br.net.woodstock.epm.office.oo.impl.SingletonOpenOfficeManager;
import br.net.woodstock.epm.office.oo.impl.SocketOpenOfficeConnection;
import br.net.woodstock.rockframework.utils.IOUtils;

public final class OpenOfficeManagerMain {

	private OpenOfficeManagerMain() {
		super();
	}

	public static void main(final String[] args) {
		try {
			InputStream input = OpenOfficeManagerMain.class.getClassLoader().getResourceAsStream("teste.ott");
			final byte[] bytes = IOUtils.toByteArray(input);
			OpenOfficeConnection connection = new SocketOpenOfficeConnection("localhost", 8100);
			final OpenOfficeManager manager = new SingletonOpenOfficeManager(connection);
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 4; i++) {

					Runnable runnable = new Runnable() {

						@Override
						public void run() {
							try {

								long l = System.currentTimeMillis();
								System.out.println("Executando " + Thread.currentThread().getName());
								ConversionExecutor template = new ConversionExecutor(new ByteArrayInputStream(bytes), OfficeDocumentType.ODT);
								InputStream output = manager.execute(template);

								File file = File.createTempFile("teste", ".docx");
								FileOutputStream outputStream = new FileOutputStream(file);
								IOUtils.copy(output, outputStream);

								output.close();
								outputStream.close();
								l = System.currentTimeMillis() - l;
								System.out.println("Resultado da " + Thread.currentThread().getName() + " em " + l + "ms, arquivo " + file.getAbsolutePath());
							} catch (Exception e) {
								System.exit(0);
								throw new RuntimeException(e);
							}
						}
					};

					Thread thread = new Thread(runnable);
					thread.setName("Thread " + i + "." + j);
					thread.start();

				}
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
