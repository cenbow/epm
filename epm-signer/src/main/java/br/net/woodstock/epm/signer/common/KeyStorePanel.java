package br.net.woodstock.epm.signer.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import br.net.woodstock.rockframework.core.utils.Conditions;
import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.PrivateKeyEntry;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.StoreAlias;
import br.net.woodstock.rockframework.security.store.StoreException;

public final class KeyStorePanel extends JPanel {

	private static final long		serialVersionUID	= -2336931609465577799L;

	private static KeyStorePanel	instance			= new KeyStorePanel();

	private JLabel					lbType;

	private JComboBox				cbType;

	private JLabel					lbCertificate;

	private JComboBox				cbCertificate;

	private JLabel					lbKeyPassword;

	private JPasswordField			txKeyPassword;

	private JButton					btNext;

	private KeyStorePanel() {
		super();
		this.init();
	}

	protected void init() {
		this.buildGUI();
		this.addGUIEvents();
		this.restoreValues();
	}

	protected void buildGUI() {
		// Layout
		this.setLayout(new GridBagLayout());

		// Line
		int line = 0;

		// Type
		this.lbType = new JLabel(SignerMessage.getMessage(Constants.LABEL_TYPE) + Constants.LABEL_SUFFIX);
		this.cbType = new JComboBox();
		this.cbType.addItem(SignerMessage.getMessage(Constants.LABEL_SELECT));
		for (StoreTypeHandler handler : ApplicationHolder.getInstance().getHandlers()) {
			this.cbType.addItem(handler);
		}

		this.add(this.lbType, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.cbType, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Certificate
		this.lbCertificate = new JLabel(SignerMessage.getMessage(Constants.LABEL_CERTIFICATE) + Constants.LABEL_SUFFIX);
		this.cbCertificate = new JComboBox();

		this.add(this.lbCertificate, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.cbCertificate, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Password
		this.lbKeyPassword = new JLabel(SignerMessage.getMessage(Constants.LABEL_PASSWORD) + Constants.LABEL_SUFFIX);
		this.txKeyPassword = new JPasswordField(20);
		this.txKeyPassword.setEditable(false);

		this.add(this.lbKeyPassword, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.txKeyPassword, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Buttons
		this.btNext = new JButton(SignerMessage.getMessage(Constants.LABEL_NEXT));
		this.btNext.setEnabled(false);

		this.add(this.btNext, SwingUtils.getConstraints(line, 0, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
	}

	protected void addGUIEvents() {
		this.cbType.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				Object selectedItem = cb.getSelectedItem();
				if (selectedItem instanceof StoreTypeHandler) {

					StoreTypeHandler handler = (StoreTypeHandler) selectedItem;
					handler.execute();
				} else {
					KeyStorePanel.this.getCbCertificate().removeAllItems();
				}
			}

		});

		this.cbCertificate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				KeyStorePanel.this.checkAlias();
			}

		});

		this.txKeyPassword.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(final KeyEvent e) {
				KeyStorePanel.this.checkStatus();
			}

		});

		this.btNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				// Verify
				Store store = ApplicationHolder.getInstance().getStore();

				if (store != null) {
					Alias selectedAlias = ApplicationHolder.getInstance().getAlias();
					StoreAlias alias = null;

					if ((Conditions.isNotEmpty(KeyStorePanel.this.getTxKeyPassword().getPassword()))) {
						alias = new PasswordAlias(selectedAlias.getName(), new String(KeyStorePanel.this.getTxKeyPassword().getPassword()));
					} else {
						alias = new StoreAlias(selectedAlias.getName());
					}

					boolean ok = false;
					try {
						PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) store.get(alias);
						if (privateKeyEntry != null) {
							ok = true;
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(KeyStorePanel.this, SignerMessage.getMessage(Constants.MSG_ERROR_OPEN_PRIVATE_KEY), SignerMessage.getMessage(Constants.LABEL_ERROR), JOptionPane.ERROR_MESSAGE);
						SignerLog.getLogger().info(ex.getMessage(), ex);
					}

					if (ok) {
						if (KeyStorePanel.this.getTxKeyPassword().isEditable()) {
							ApplicationHolder.getInstance().setPassword(new String(KeyStorePanel.this.getTxKeyPassword().getPassword()));
						}

						ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(0, false);
						ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(1, true);
						ApplicationPanel.getInstance().getTabbedPane().setSelectedIndex(1);
					}
				} else {
					JOptionPane.showMessageDialog(KeyStorePanel.this, SignerMessage.getMessage(Constants.MSG_ERROR_NO_KEYSTORE), SignerMessage.getMessage(Constants.LABEL_ERROR), JOptionPane.ERROR_MESSAGE);
				}
			}

		});
	}

	protected void restoreValues() {
		//StoreTypeHandler handler = ApplicationHolder.getInstance().getHandler();
		//if (handler != null) {
		//	this.cbType.setSelectedItem(handler);
		//}
	}

	public void onSelectStore() {
		Store store = ApplicationHolder.getInstance().getStore();

		if (store == null) {
			this.cbType.setSelectedIndex(0);
		}

		if (this.cbCertificate.getItemCount() > 0) {
			this.cbCertificate.removeAllItems();
		}
		if (store != null) {
			for (Alias alias : store.aliases()) {
				this.cbCertificate.addItem(alias);
			}
		}

		if (this.cbCertificate.getItemCount() == 1) {
			this.checkAlias();
		}
	}

	protected void checkStatus() {
		boolean enabled = true;
		if (this.cbType.getSelectedItem() instanceof String) {
			enabled = false;
		}
		if (!(this.cbCertificate.getSelectedItem() instanceof Alias)) {
			enabled = false;
		}
		if ((this.txKeyPassword.isEditable() && (Conditions.isEmpty(this.txKeyPassword.getPassword())))) {
			enabled = false;
		}
		this.btNext.setEnabled(enabled);
	}

	protected void checkAlias() {
		Store store = ApplicationHolder.getInstance().getStore();
		try {
			Alias selectedAlias = (Alias) this.cbCertificate.getSelectedItem();
			ApplicationHolder.getInstance().setAlias(selectedAlias);
			if (selectedAlias != null) {
				StoreAlias alias = new StoreAlias(selectedAlias.getName());
				store.get(alias);
				this.txKeyPassword.setText(null);
				this.txKeyPassword.setEditable(false);
			} else {
				this.txKeyPassword.setText(null);
				this.txKeyPassword.setEditable(false);
			}
		} catch (StoreException e) {
			this.txKeyPassword.setEditable(true);
		}
	}

	public JComboBox getCbType() {
		return this.cbType;
	}

	public JComboBox getCbCertificate() {
		return this.cbCertificate;
	}

	public JPasswordField getTxKeyPassword() {
		return this.txKeyPassword;
	}

	// Instance
	public static KeyStorePanel getInstance() {
		return KeyStorePanel.instance;
	}

}
