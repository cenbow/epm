package br.net.woodstock.epm.signer.common;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileMutableTreeNode extends DefaultMutableTreeNode {

	private static final long	serialVersionUID	= 9162329156689082080L;

	public FileMutableTreeNode(final FileObject fileObject) {
		super(fileObject);
	}

	@Override
	public boolean isLeaf() {
		FileObject fileObject = (FileObject) this.getUserObject();
		File file = fileObject.getFile();
		if (file.isDirectory()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean getAllowsChildren() {
		FileObject fileObject = (FileObject) this.getUserObject();
		File file = fileObject.getFile();
		if (file.isDirectory()) {
			return true;
		}
		return false;
	}

	// Aux
	public File getFile() {
		FileObject fileObject = (FileObject) this.getUserObject();
		if (fileObject != null) {
			return fileObject.getFile();
		}
		return null;
	}

}
