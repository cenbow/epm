package br.net.woodstock.epm.office.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.epm.office.OfficeDocumentType;
import br.net.woodstock.epm.office.convert.ConversionManager;
import br.net.woodstock.rockframework.utils.IOUtils;

@RunWith(BlockJUnit4ClassRunner.class)
public class ConvertTest {

	public ConvertTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		FileInputStream fileInputStream = new FileInputStream("/tmp/teste.odt");
		byte[] input = IOUtils.toByteArray(fileInputStream);
		fileInputStream.close();

		byte[] output = new ConversionManager(8100).convert(input, OfficeDocumentType.ODT, OfficeDocumentType.DOCX);

		FileOutputStream outputStream = new FileOutputStream("/tmp/teste3.docx");
		outputStream.write(output);
		outputStream.close();
	}

}
