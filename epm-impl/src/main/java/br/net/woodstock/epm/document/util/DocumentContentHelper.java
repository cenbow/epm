package br.net.woodstock.epm.document.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import br.net.woodstock.rockframework.utils.ConditionUtils;

abstract class DocumentContentHelper {

	private static final String	VALUE_SEPARATOR	= ", ";

	private DocumentContentHelper() {
		//
	}

	public static DocumentContent getDocumentContent(final byte[] bytes) throws IOException, SAXException, TikaException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		TikaInputStream stream = TikaInputStream.get(bytes);
		ContentHandler handler = new BodyContentHandler(outputStream);
		ParseContext context = new ParseContext();
		Metadata metadata = new Metadata();
		Parser parser = new AutoDetectParser();
		context.set(Parser.class, parser);

		parser.parse(stream, handler, metadata, context);

		String[] names = metadata.names();
		String text = new String(outputStream.toByteArray());

		Map<String, String> map = new HashMap<String, String>();
		for (String name : names) {
			map.put(name, DocumentContentHelper.toString(metadata.getValues(name)));
		}

		return new DefaultDocumentContent(text, metadata.get(HttpHeaders.CONTENT_TYPE), map);
	}

	private static class DefaultDocumentContent extends DocumentContent {

		private static final long	serialVersionUID	= 1L;

		public DefaultDocumentContent(final String text, final String mimeType, final Map<String, String> metadata) {
			super(text, mimeType, metadata);
		}

	}

	private static String toString(final String[] values) {
		if (ConditionUtils.isEmpty(values)) {
			return null;
		}
		if (values.length == 1) {
			return values[0];
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String s : values) {
			if (!first) {
				builder.append(DocumentContentHelper.VALUE_SEPARATOR);
			}
			builder.append(s);
			if (first) {
				first = false;
			}
		}
		return builder.toString();
	}

}
