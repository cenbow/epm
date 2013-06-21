package br.net.woodstock.epm.signer.core;

import java.io.File;
import java.util.Comparator;

public final class FileComparator implements Comparator<File> {

	private static FileComparator	instance	= new FileComparator();

	private FileComparator() {
		super();
	}

	@Override
	public int compare(final File f1, final File f2) {
		if (((f1.isDirectory()) && (f2.isDirectory())) || ((!f1.isDirectory()) && (!f2.isDirectory()))) {
			return f1.getName().compareTo(f2.getName());
		}
		if (f1.isDirectory()) {
			return -1;
		}
		if (f2.isDirectory()) {
			return 1;
		}
		return f1.getName().compareTo(f2.getName());
	}

	public static FileComparator getInstance() {
		return FileComparator.instance;
	}

}
