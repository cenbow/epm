package br.net.woodstock.epm.impl.test;

import java.io.File;

import org.junit.Test;

import br.net.woodstock.epm.search.lucene.util.MetadataUtils;
import br.net.woodstock.rockframework.utils.IOUtils;

public class MetadataTest {

	public MetadataTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		File file = new File("/home/lourival/Documents/ponto_08.odt");
		byte[] bytes = IOUtils.toByteArray(file);
		MetadataUtils.getMetadata(bytes);
	}

}
