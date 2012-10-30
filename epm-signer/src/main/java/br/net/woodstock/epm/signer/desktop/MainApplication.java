package br.net.woodstock.epm.signer.desktop;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import br.net.woodstock.epm.signer.common.ApplicationPanel;
import br.net.woodstock.epm.signer.common.Constants;

public abstract class MainApplication {

	private MainApplication() {
		super();
	}

	public static void createGUI() {
		JFrame frame = new JFrame(Constants.APP_NAME);
		frame.setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
		frame.add(ApplicationPanel.getInstance());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainApplication.createGUI();
			}

		});
	}

}
