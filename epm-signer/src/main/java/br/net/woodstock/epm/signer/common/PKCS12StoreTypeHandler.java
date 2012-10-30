package br.net.woodstock.epm.signer.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import br.net.woodstock.rockframework.security.ProviderType;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.impl.JCAStore;
import br.net.woodstock.rockframework.utils.ConditionUtils;

public class PKCS12StoreTypeHandler implements StoreTypeHandler {

	private static final String	TYPE_NAME	= "PKCS12 File";

	private static final String	PROVIDER	= ProviderType.BOUNCY_CASTLE.getType();

	private JFrame				frame;

	private JLabel				lbStore;

	private JTextField			txStore;

	private JButton				btStore;

	private JLabel				lbStorePassword;

	private JPasswordField		txStorePassword;

	private JButton				btOpen;

	private Store				store;

	public PKCS12StoreTypeHandler() {
		super();
		this.init();
	}

	protected void init() {
		// Layout
		this.frame = new JFrame();
		this.frame.setTitle(ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_TYPE));
		this.frame.setLayout(new GridBagLayout());

		// Line
		int line = 0;

		// Store
		this.lbStore = new JLabel(ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_FILE) + Constants.LABEL_SUFFIX);

		this.txStore = new JTextField(25);
		this.txStore.setEditable(false);

		this.btStore = new JButton(ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_SEARCH));
		this.btStore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showDialog(PKCS12StoreTypeHandler.this.getFrame(), ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_OK));
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					PKCS12StoreTypeHandler.this.onSelectFile(file);
				}

			}
		});

		this.frame.add(this.lbStore, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.frame.add(this.txStore, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		this.frame.add(this.btStore, SwingUtils.getConstraints(line, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Password
		this.lbStorePassword = new JLabel(ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_PASSWORD) + Constants.LABEL_SUFFIX);
		this.txStorePassword = new JPasswordField(20);
		this.txStorePassword.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(final KeyEvent e) {
				PKCS12StoreTypeHandler.this.checkStatus();
			}

		});

		this.frame.add(this.lbStorePassword, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.frame.add(this.txStorePassword, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Buttons
		this.btOpen = new JButton(ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_OPEN));
		this.btOpen.setEnabled(false);
		this.btOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				PKCS12StoreTypeHandler.this.openKeyStore();
			}

		});

		this.frame.add(this.btOpen, SwingUtils.getConstraints(line, 0, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
		this.frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowActivated(final WindowEvent e) {
				ApplicationHolder.getInstance().setHandler(PKCS12StoreTypeHandler.this);
				ApplicationHolder.getInstance().setStore(PKCS12StoreTypeHandler.this.getStore());
			}

			@Override
			public void windowClosing(final WindowEvent e) {
				//ApplicationHolder.getInstance().onSelectStore();
			}

		});
		this.frame.pack();
	}

	@Override
	public void execute() {
		ApplicationHolder.getInstance().setHandler(this);
		this.frame.setVisible(true);
	}

	protected void onSelectFile(final File file) {
		this.txStore.setText(file.getPath());
		this.checkStatus();
	}

	protected void checkStatus() {
		String file = this.txStore.getText();
		char[] password = this.txStorePassword.getPassword();
		if ((ConditionUtils.isNotEmpty(file)) && (ConditionUtils.isNotEmpty(password))) {
			this.btOpen.setEnabled(true);
		} else {
			this.btOpen.setEnabled(false);
		}
	}

	protected void openKeyStore() {
		FileInputStream inputStream = null;
		try {
			String keyStorePassword = new String(this.txStorePassword.getPassword());
			inputStream = new FileInputStream(this.txStore.getText());
			this.store = new JCAStore(KeyStoreType.PKCS12);
			this.store.read(inputStream, keyStorePassword);
			ApplicationHolder.getInstance().setStore(this.store);
			ApplicationHolder.getInstance().onSelectStore();
			this.frame.setVisible(false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.frame, e, ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_ERROR), JOptionPane.ERROR_MESSAGE);
			SignerLog.getLogger().debug(e.getMessage(), e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					SignerLog.getLogger().debug(e.getMessage(), e);
				}
			}
		}
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public Store getStore() {
		return this.store;
	}

	@Override
	public String getProvider() {
		return PKCS12StoreTypeHandler.PROVIDER;
	}

	@Override
	public String toString() {
		return PKCS12StoreTypeHandler.TYPE_NAME;
	}

}
