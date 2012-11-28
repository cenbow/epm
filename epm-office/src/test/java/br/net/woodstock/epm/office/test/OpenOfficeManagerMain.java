package br.net.woodstock.epm.office.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.oo.ConversionExecutor;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;
import br.net.woodstock.rockframework.utils.IOUtils;

public class OpenOfficeManagerMain {

	public OpenOfficeManagerMain() {
		super();
	}

	public static void main(String[] args) {
		try {
			InputStream input = OpenOfficeManagerMain.class.getClassLoader().getResourceAsStream("teste.ott");
			final byte[] bytes = IOUtils.toByteArray(input);
			final OpenOfficeManager manager = new OpenOfficeManager(8100);
			for (int j = 0; j < 16; j++) {
				for (int i = 0; i < 16; i++) {

					Runnable runnable = new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep((new Random().nextInt(10) + 1) * 1000);
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
					thread.setName("Thread " + i);
					thread.start();

				}
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
