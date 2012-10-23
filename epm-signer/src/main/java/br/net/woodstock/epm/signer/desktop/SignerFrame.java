package br.net.woodstock.epm.signer.desktop;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import br.net.woodstock.epm.signer.common.Constants;
import br.net.woodstock.epm.signer.common.SignerPanel;

public abstract class SignerFrame {

	private SignerFrame() {
		super();
	}

	public static void createGUI() {
		JFrame frame = new JFrame(Constants.APP_NAME);
		frame.setSize(Constants.APP_WIDTH, Constants.APP_HEIGHT);
		frame.add(new SignerPanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				SignerFrame.createGUI();
			}

		});
	}

}
