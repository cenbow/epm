package br.net.woodstock.epm.office.openoffice;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import br.net.woodstock.epm.office.OfficeDocument;

public class OpenOfficeUnoClient implements Serializable {

	private static final long	serialVersionUID	= 3146278934728167261L;

	private URL					url;

	public OpenOfficeUnoClient(final URL url) {
		super();
		this.url = url;
	}

	public OpenOfficeUnoClient(final String url) throws MalformedURLException {
		this(new URL(url));
	}

	public OfficeDocument openDocument(final byte[] bytes) {
		return OpenOfficeHelper.openDocument(this.url, bytes);
	}

}
