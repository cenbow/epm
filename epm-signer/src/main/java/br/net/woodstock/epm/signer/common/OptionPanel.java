package br.net.woodstock.epm.signer.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class OptionPanel extends JPanel {

	private static final long	serialVersionUID	= -2336931609465577799L;

	private static OptionPanel	instance			= new OptionPanel();

	private JLabel				lbP7s;

	private JCheckBox			ckP7sDetached;

	private JLabel				lbPdf;

	private JCheckBox			ckPdfEmbedded;

	private JLabel				lbTimeStampURL;

	private JTextField			txTimeStampURL;

	private JButton				btBack;

	private JButton				btNext;

	private OptionPanel() {
		super();
		this.init();
	}

	protected void init() {
		this.buildGUI();
		this.addGUIEvents();
	}

	protected void buildGUI() {
		// Layout
		this.setLayout(new GridBagLayout());

		// Line
		int line = 0;

		// P7S
		this.lbP7s = new JLabel(SignerMessage.getMessage(Constants.LABEL_P7S_OPTIONS) + Constants.LABEL_SUFFIX);
		this.ckP7sDetached = new JCheckBox(SignerMessage.getMessage(Constants.LABEL_P7S_DETACHED));
		this.ckP7sDetached.setEnabled(false);

		this.add(this.lbP7s, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.ckP7sDetached, SwingUtils.getConstraints(line, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// PDF
		this.lbPdf = new JLabel(SignerMessage.getMessage(Constants.LABEL_PDF_OPTIONS) + Constants.LABEL_SUFFIX);
		this.ckPdfEmbedded = new JCheckBox(SignerMessage.getMessage(Constants.LABEL_PDF_EMBEDDED));
		this.ckPdfEmbedded.setEnabled(false);

		this.add(this.lbPdf, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.ckPdfEmbedded, SwingUtils.getConstraints(line, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// TimeStamp
		this.lbTimeStampURL = new JLabel(SignerMessage.getMessage(Constants.LABEL_TIMESTAMP) + Constants.LABEL_SUFFIX);
		this.txTimeStampURL = new JTextField(30);

		this.add(this.lbTimeStampURL, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.txTimeStampURL, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Buttons
		this.btBack = new JButton(SignerMessage.getMessage(Constants.LABEL_BACK));
		// this.btBack.setEnabled(false);
		this.add(this.btBack, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));

		this.btNext = new JButton(SignerMessage.getMessage(Constants.LABEL_NEXT));
		// this.btNext.setEnabled(false);
		this.add(this.btNext, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
	}

	protected void addGUIEvents() {
		this.ckPdfEmbedded.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				OptionPanel.this.onSelectPDFCK();
			}
		});

		this.btBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(2, false);
				ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(1, true);
				ApplicationPanel.getInstance().getTabbedPane().setSelectedIndex(1);
			}

		});

		this.btNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(2, false);
				ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(3, true);
				ApplicationPanel.getInstance().getTabbedPane().setSelectedIndex(3);
			}

		});
	}

	protected void onSelectPDFCK() {
		if (this.ckPdfEmbedded.isSelected()) {
			this.ckP7sDetached.setEnabled(false);
			this.ckP7sDetached.setSelected(false);
		} else {
			this.ckP7sDetached.setEnabled(true);
			this.ckP7sDetached.setSelected(true);
		}
	}

	// Instance
	public static OptionPanel getInstance() {
		return OptionPanel.instance;
	}

}
