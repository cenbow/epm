package br.net.woodstock.epm.signer.common;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public final class ApplicationPanel extends JPanel {

	private static final long		serialVersionUID	= 5889530862120029930L;

	private static ApplicationPanel	instance			= new ApplicationPanel();

	private JTabbedPane				tabbedPane;

	private ApplicationPanel() {
		super();
		this.init();
	}

	protected void init() {
		this.buildGUI();
		this.addGUIEvents();
	}

	protected void buildGUI() {
		this.tabbedPane = new JTabbedPane();

		this.tabbedPane.addTab("KeyStore", KeyStorePanel.getInstance());
		this.tabbedPane.addTab("Options", new OptionPanel());
		this.tabbedPane.addTab("Confirm", new ConfirmPanel());

		this.tabbedPane.setEnabledAt(1, false);
		this.tabbedPane.setEnabledAt(2, false);

		this.add(this.tabbedPane);
	}

	protected void addGUIEvents() {
		//
	}

	public JTabbedPane getTabbedPane() {
		return this.tabbedPane;
	}

	public static ApplicationPanel getInstance() {
		return ApplicationPanel.instance;
	}

}
