package br.net.woodstock.epm.web;

import java.io.Serializable;

public abstract class AbstractSearch implements Serializable {

	private static final long	serialVersionUID	= -4313009035332736312L;

	private String				selectedIndex;

	public String getSelectedIndex() {
		return this.selectedIndex;
	}

	public void setSelectedIndex(String selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	// Aux
	public int getFirstRow() {
		if (this.getSelectedIndex() == null) {
			return 0;
		}
		//int selectedIndex = this.getSelectedIndex().intValue();
		//return selectedIndex;
		return 0;
	}

}
