package br.net.woodstock.epm.document.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public abstract class DocumentContent implements Serializable {

	private static final long	serialVersionUID	= -3551274356132204092L;

	private String				text;

	private String				mimeType;

	private Map<String, String>	metadata;

	public DocumentContent(final String text, final String mimeType, final Map<String, String> metadata) {
		super();
		this.text = text;
		this.mimeType = mimeType;
		this.metadata = metadata;
	}

	public String getText() {
		return this.text;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public Map<String, String> getMetadata() {
		return this.metadata;
	}

	public static DocumentContent getInstance(final byte[] bytes) throws IOException, SAXException, TikaException {
		return DocumentContentHelper.getDocumentContent(bytes);
	}

}
