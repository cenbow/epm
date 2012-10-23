package br.net.woodstock.epm.signer.common;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public abstract class SwingUtils {

	private SwingUtils() {
		//
	}

	public static GridBagConstraints getConstraints(final int row, final int col, final int width, final int height, final int align, final int fill) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = align;
		constraints.fill = fill;
		constraints.gridx = col;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.ipadx = 5;
		constraints.ipady = 5;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.insets = new Insets(5, 5, 5, 5);
		return constraints;
	}

}
