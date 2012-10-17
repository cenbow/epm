package br.net.woodstock.epm.search.lucene.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import br.net.woodstock.rockframework.utils.ArrayUtils;

public abstract class MetadataUtils {

	private static final Tika	TIKA	= new Tika();

	private MetadataUtils() {
		//
	}

	public static void getMetadata(final byte[] bytes) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
		Metadata metadata = new Metadata();
		MetadataUtils.TIKA.parse(inputStream, metadata);
		String[] names = metadata.names();
		for (String name : names) {
			System.out.println(name + " => " + ArrayUtils.toString(metadata.getValues(name)));
		}
	}

}
