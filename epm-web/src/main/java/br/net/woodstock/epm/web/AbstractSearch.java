package br.net.woodstock.epm.web;

import java.io.Serializable;

public abstract class AbstractSearch implements Serializable {

	private static final long	serialVersionUID	= -4313009035332736312L;

	private Integer				selectedIndex;

	public Integer getSelectedIndex() {
		return this.selectedIndex;
	}

	public void setSelectedIndex(final Integer selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	// Aux
	public int getFirstRow() {
		if (this.getSelectedIndex() == null) {
			return 0;
		}
		int selectedIndex = this.getSelectedIndex().intValue();
		if (selectedIndex > 0) {
			selectedIndex = (selectedIndex / WebConstants.PAGE_SIZE) * WebConstants.PAGE_SIZE;
		}
		return selectedIndex;
	}

}
