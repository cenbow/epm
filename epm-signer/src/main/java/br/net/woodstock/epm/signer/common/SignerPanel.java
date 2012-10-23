package br.net.woodstock.epm.signer.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.sign.PKCS7SignatureMode;
import br.net.woodstock.rockframework.security.sign.PKCS7SignatureParameters;
import br.net.woodstock.rockframework.security.sign.SignatureInfo;
import br.net.woodstock.rockframework.security.sign.Signer;
import br.net.woodstock.rockframework.security.sign.impl.BouncyCastlePKCS7Signer;
import br.net.woodstock.rockframework.security.sign.impl.PDFSigner;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.StoreEntryType;
import br.net.woodstock.rockframework.security.store.StoreException;
import br.net.woodstock.rockframework.utils.ConditionUtils;
import br.net.woodstock.rockframework.utils.FileUtils;
import br.net.woodstock.rockframework.utils.IOUtils;

public class SignerPanel extends JPanel {

	private static final long	serialVersionUID	= -2336931609465577799L;

	private JLabel				lbType;

	private JComboBox			cbType;

	private JLabel				lbCertificate;

	private JComboBox			cbCertificate;

	private JLabel				lbKeyPassword;

	private JPasswordField		txKeyPassword;

	private JLabel				lbFile;

	private JTextField			txFile;

	private JButton				btFile;

	private JLabel				lbDir;

	private JTextField			txDir;

	private JButton				btDir;

	private JLabel				lbP7s;

	private JCheckBox			ckP7sDetached;

	private JLabel				lbPdf;

	private JCheckBox			ckPdfEmbedded;

	private JLabel				lbOutput;

	private JTextField			txOutput;

	private JButton				btSign;

	public SignerPanel() {
		super();
		SignerHolder.getInstance().setPanel(this);
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

		// Type
		this.lbType = new JLabel(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_TYPE) + Constants.LABEL_SUFFIX);
		this.cbType = new JComboBox(new Object[] { SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_SELECT), new PKCS12StoreTypeHandler() });

		this.add(this.lbType, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.cbType, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Certificate
		this.lbCertificate = new JLabel(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_CERTIFICATE) + Constants.LABEL_SUFFIX);
		this.cbCertificate = new JComboBox();

		this.add(this.lbCertificate, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.cbCertificate, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Password
		this.lbKeyPassword = new JLabel(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_PASSWORD) + Constants.LABEL_SUFFIX);
		this.txKeyPassword = new JPasswordField(20);
		this.txKeyPassword.setEditable(false);

		this.add(this.lbKeyPassword, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.txKeyPassword, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// File
		this.lbFile = new JLabel(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_FILE) + Constants.LABEL_SUFFIX);

		this.txFile = new JTextField(25);
		this.txFile.setEditable(false);

		this.btFile = new JButton(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_SEARCH));

		this.add(this.lbFile, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.txFile, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		this.add(this.btFile, SwingUtils.getConstraints(line, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Dir
		this.lbDir = new JLabel(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_DIR) + Constants.LABEL_SUFFIX);

		this.txDir = new JTextField(25);
		this.txDir.setEditable(false);

		this.btDir = new JButton(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_SEARCH));

		this.add(this.lbDir, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.txDir, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		this.add(this.btDir, SwingUtils.getConstraints(line, 2, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// P7S
		this.lbP7s = new JLabel(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_P7S_OPTIONS) + Constants.LABEL_SUFFIX);
		this.ckP7sDetached = new JCheckBox(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_P7S_DETACHED));
		this.ckP7sDetached.setEnabled(false);

		this.add(this.lbP7s, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.ckP7sDetached, SwingUtils.getConstraints(line, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// PDF
		this.lbPdf = new JLabel(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_PDF_OPTIONS) + Constants.LABEL_SUFFIX);
		this.ckPdfEmbedded = new JCheckBox(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_PDF_EMBEDDED));
		this.ckPdfEmbedded.setEnabled(false);

		this.add(this.lbPdf, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.ckPdfEmbedded, SwingUtils.getConstraints(line, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Output
		this.lbOutput = new JLabel(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_OUTPUT) + Constants.LABEL_SUFFIX);
		this.txOutput = new JTextField(25);
		this.txOutput.setEditable(false);

		this.add(this.lbOutput, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE));
		this.add(this.txOutput, SwingUtils.getConstraints(line, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.NONE));
		line++;

		// Buttons
		this.btSign = new JButton(SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_SIGN));
		this.btSign.setEnabled(false);

		this.add(this.btSign, SwingUtils.getConstraints(line, 0, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
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
					SignerPanel.this.getCbCertificate().removeAllItems();
				}
			}

		});

		this.cbCertificate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				SignerPanel.this.checkAlias();
			}

		});

		this.txKeyPassword.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(final KeyEvent e) {
				SignerPanel.this.checkStatus();
			}

		});

		this.btFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showDialog(SignerPanel.this, SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_OK));
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					SignerPanel.this.onSelectFile(file);
				}

			}
		});

		this.btDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showDialog(SignerPanel.this, SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_OK));
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					SignerPanel.this.onSelectOutput(file);
				}

			}
		});

		this.ckPdfEmbedded.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				SignerPanel.this.onSelectPDFCK();
			}
		});

		this.btSign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				SignerPanel.this.signFile();
			}

		});

	}

	public void onSelectStore() {
		Store store = SignerHolder.getInstance().getStore();

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
		this.checkStatus();
	}

	protected void onSelectFile(final File file) {
		this.txFile.setText(file.getPath());

		File parent = file.getParentFile();
		if ((parent != null) && (parent.canWrite())) {
			this.txDir.setText(parent.getPath());
		}

		String extension = FileUtils.getExtension(file);
		if (Constants.PDF_FILE_EXTENSION.equals(extension)) {
			this.ckPdfEmbedded.setEnabled(true);
			this.ckPdfEmbedded.setSelected(true);
			this.ckP7sDetached.setEnabled(false);
			this.ckP7sDetached.setSelected(false);
		} else {
			this.ckPdfEmbedded.setEnabled(false);
			this.ckP7sDetached.setEnabled(true);
			this.ckP7sDetached.setSelected(true);
		}
		this.checkStatus();
		this.checkOutputName();
	}

	protected void onSelectOutput(final File file) {
		if (file.canWrite()) {
			this.txDir.setText(file.getPath());
		} else {
			this.txDir.setText(null);
		}
		this.checkStatus();
		this.checkOutputName();
	}

	protected void onSelectPDFCK() {
		if (this.ckPdfEmbedded.isSelected()) {
			this.ckP7sDetached.setEnabled(false);
			this.ckP7sDetached.setSelected(false);
		} else {
			this.ckP7sDetached.setEnabled(true);
			this.ckP7sDetached.setSelected(true);
		}
		this.checkOutputName();
	}

	protected void checkStatus() {
		boolean enabled = true;
		if (this.cbType.getSelectedItem() instanceof String) {
			enabled = false;
		}
		if (!(this.cbCertificate.getSelectedItem() instanceof Alias)) {
			enabled = false;
		}
		if ((this.txKeyPassword.isEnabled() && (ConditionUtils.isEmpty(this.txKeyPassword.getPassword())))) {
			enabled = false;
		}
		if (ConditionUtils.isEmpty(this.txFile.getText())) {
			enabled = false;
		}
		if (ConditionUtils.isEmpty(this.txDir.getText())) {
			enabled = false;
		}
		this.btSign.setEnabled(enabled);
	}

	protected void checkAlias() {
		Store store = SignerHolder.getInstance().getStore();
		try {
			Alias selectedAlias = (Alias) this.cbCertificate.getSelectedItem();
			if (selectedAlias != null) {
				Alias alias = new Alias(selectedAlias.getName());
				store.get(alias, StoreEntryType.PRIVATE_KEY);
				this.txKeyPassword.setText(null);
				this.txKeyPassword.setEditable(false);
			} else {
				this.txKeyPassword.setText(null);
				this.txKeyPassword.setEditable(false);
			}
		} catch (StoreException e) {
			this.txKeyPassword.setEditable(true);
		}
		this.checkStatus();
	}

	protected void checkOutputName() {
		if (ConditionUtils.isNotEmpty(this.txFile.getText())) {
			File file = new File(this.txFile.getText());
			String fileName = FileUtils.getName(file);
			String extension = FileUtils.getExtension(file);

			String newFileName = null;

			if (Constants.PDF_FILE_EXTENSION.equals(extension)) {
				if (this.ckPdfEmbedded.isSelected()) {
					newFileName = fileName.substring(0, fileName.lastIndexOf(extension) - 1) + Constants.PDF_FILE_SUFFIX;
				} else {
					newFileName = fileName + Constants.P7S_FILE_SUFFIX;
				}
			} else {
				newFileName = fileName + Constants.P7S_FILE_SUFFIX;
			}

			String fullName = this.txDir.getText() + File.separator + newFileName;
			this.txOutput.setText(fullName);
		} else {
			this.txOutput.setText(null);
		}
	}

	protected void signFile() {
		File file = new File(this.txFile.getText());
		String extension = FileUtils.getExtension(file);
		boolean pdf = Constants.PDF_FILE_EXTENSION.equals(extension);
		this.signFileInternal(pdf);
	}

	private void signFileInternal(final boolean pdf) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(this.txFile.getText());
			Alias selectedAlias = (Alias) this.cbCertificate.getSelectedItem();
			Alias alias = null;

			if ((this.txKeyPassword.isEditable()) && (ConditionUtils.isNotEmpty(this.txKeyPassword.getPassword()))) {
				alias = new PasswordAlias(selectedAlias.getName(), new String(this.txKeyPassword.getPassword()));
			} else {
				alias = new Alias(selectedAlias.getName());
			}

			PKCS7SignatureMode mode = null;
			if (!pdf) {
				if (this.ckP7sDetached.isSelected()) {
					mode = PKCS7SignatureMode.DETACHED;
				} else {
					mode = PKCS7SignatureMode.ATTACHED;
				}
			}

			Store store = SignerHolder.getInstance().getStore();
			SignatureInfo signatureInfo = new SignatureInfo();
			PKCS7SignatureParameters parameters = new PKCS7SignatureParameters(alias, store, signatureInfo, null, mode);

			Signer signer = null;
			if ((pdf) && (this.ckPdfEmbedded.isSelected())) {
				signer = new PDFSigner(parameters);
			} else {
				signer = new BouncyCastlePKCS7Signer(parameters);
			}

			byte[] bytes = IOUtils.toByteArray(inputStream);
			byte[] signature = signer.sign(bytes);
			File outputFile = new File(this.txOutput.getText());
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(signature);
			outputStream.close();

			JOptionPane.showMessageDialog(this, SignerHolder.getInstance().getMessage().getMessage(Constants.MSG_SUCCESS), SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_INFO), JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex, SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_ERROR), JOptionPane.ERROR_MESSAGE);
			SignerLog.getLogger().warn(ex.getMessage(), ex);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					SignerLog.getLogger().debug(ex.getMessage(), ex);
				}
			}
		}
	}

	public JComboBox getCbCertificate() {
		return this.cbCertificate;
	}

}
