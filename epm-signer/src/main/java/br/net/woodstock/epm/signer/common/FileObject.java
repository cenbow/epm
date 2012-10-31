package br.net.woodstock.epm.signer.common;

import java.io.File;
import java.io.Serializable;

public class FileObject implements Serializable {

	private static final long	serialVersionUID	= -4208571100100309501L;

	private String				name;

	private File				file;

	public FileObject(final String name, final File file) {
		super();
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return this.name;
	}

	public File getFile() {
		return this.file;
	}

	@Override
	public String toString() {
		return this.getName();
	}

}
