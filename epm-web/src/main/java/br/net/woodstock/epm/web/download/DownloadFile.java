package br.net.woodstock.epm.web.download;

import java.io.Serializable;

public class DownloadFile implements Serializable {

	private static final long	serialVersionUID	= 7521001324289651183L;

	private String				name;

	private byte[]				content;

	private String				contentType;

	public DownloadFile() {
		super();
	}

	public DownloadFile(final String name, final byte[] content, final String contentType) {
		super();
		this.name = name;
		this.content = content;
		this.contentType = contentType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

}
