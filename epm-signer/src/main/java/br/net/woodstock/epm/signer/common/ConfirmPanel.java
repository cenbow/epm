package br.net.woodstock.epm.signer.common;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.Identity;
import br.net.woodstock.rockframework.security.sign.PKCS7SignatureMode;
import br.net.woodstock.rockframework.security.sign.PKCS7SignatureParameters;
import br.net.woodstock.rockframework.security.sign.SignatureInfo;
import br.net.woodstock.rockframework.security.sign.Signer;
import br.net.woodstock.rockframework.security.sign.impl.BouncyCastlePKCS7Signer;
import br.net.woodstock.rockframework.security.sign.impl.PDFSigner;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.PrivateKeyEntry;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.StoreAlias;
import br.net.woodstock.rockframework.security.timestamp.TimeStampClient;
import br.net.woodstock.rockframework.security.timestamp.impl.URLTimeStampClient;
import br.net.woodstock.rockframework.utils.ConditionUtils;
import br.net.woodstock.rockframework.utils.FileUtils;
import br.net.woodstock.rockframework.utils.IOUtils;

public final class ConfirmPanel extends JPanel {

	private static final long	serialVersionUID	= -174842827285258834L;

	private static ConfirmPanel	instance			= new ConfirmPanel();

	private JButton				btBack;

	private JButton				btSign;

	private ConfirmPanel() {
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

		// Buttons
		this.btBack = new JButton(SignerMessage.getMessage(Constants.LABEL_BACK));
		this.add(this.btBack, SwingUtils.getConstraints(line, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));

		this.btSign = new JButton(SignerMessage.getMessage(Constants.LABEL_SIGN));
		this.add(this.btSign, SwingUtils.getConstraints(line, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
	}

	protected void addGUIEvents() {

		this.btBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(3, false);
				ApplicationPanel.getInstance().getTabbedPane().setEnabledAt(2, true);
				ApplicationPanel.getInstance().getTabbedPane().setSelectedIndex(2);
			}

		});

		this.btSign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				ConfirmPanel.this.signFiles();
			}

		});

	}

	protected void signFiles() {
		for (File file : ApplicationHolder.getInstance().getSelectedFiles()) {

			this.signFileInternal(file);
		}
	}

	private void signFileInternal(final File file) {
		InputStream inputStream = null;
		try {
			File outputFile = this.getOutputFile(file);
			String extension = FileUtils.getExtension(file);
			boolean pdf = Constants.PDF_FILE_EXTENSION.equals(extension);

			inputStream = new FileInputStream(file);
			Alias selectedAlias = ApplicationHolder.getInstance().getAlias();
			StoreAlias alias = null;

			if ((ConditionUtils.isNotEmpty(ApplicationHolder.getInstance().getPassword()))) {
				alias = new PasswordAlias(selectedAlias.getName(), new String(ApplicationHolder.getInstance().getPassword()));
			} else {
				alias = new StoreAlias(selectedAlias.getName());
			}

			TimeStampClient timeStampClient = null;
			if (ConditionUtils.isNotEmpty(ApplicationHolder.getInstance().getTimeStampUrl())) {
				timeStampClient = new URLTimeStampClient(ApplicationHolder.getInstance().getTimeStampUrl());
			}

			PKCS7SignatureMode mode = null;
			if (!pdf) {
				if (ApplicationHolder.getInstance().isP7sDetached()) {
					mode = PKCS7SignatureMode.DETACHED;
				} else {
					mode = PKCS7SignatureMode.ATTACHED;
				}
			}

			Store store = ApplicationHolder.getInstance().getStore();
			PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) store.get(alias);
			Identity identity = privateKeyEntry.toIdentity();
			SignatureInfo signatureInfo = new SignatureInfo();
			PKCS7SignatureParameters parameters = new PKCS7SignatureParameters(identity, signatureInfo, timeStampClient, mode);
			parameters.setProvider(ApplicationHolder.getInstance().getHandler().getProvider());

			Signer signer = null;
			if ((pdf) && (ApplicationHolder.getInstance().isPdf())) {
				signer = new PDFSigner(parameters);
			} else {
				signer = new BouncyCastlePKCS7Signer(parameters);
			}

			byte[] bytes = IOUtils.toByteArray(inputStream);
			byte[] signature = signer.sign(bytes);
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(signature);
			outputStream.close();

			JOptionPane.showMessageDialog(this, SignerMessage.getMessage(Constants.MSG_SUCCESS), SignerMessage.getMessage(Constants.LABEL_INFO), JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, SignerMessage.getMessage(Constants.MSG_ERROR_SIGN), SignerMessage.getMessage(Constants.LABEL_ERROR), JOptionPane.ERROR_MESSAGE);
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

	protected File getOutputFile(final File file) {
		String fileName = FileUtils.getName(file);
		String extension = FileUtils.getExtension(file);

		String newFileName = null;

		if (Constants.PDF_FILE_EXTENSION.equals(extension)) {
			if (ApplicationHolder.getInstance().isPdf()) {
				newFileName = fileName.substring(0, fileName.lastIndexOf(extension) - 1) + Constants.PDF_FILE_SUFFIX;
			} else {
				newFileName = fileName + Constants.P7S_FILE_SUFFIX;
			}
		} else {
			newFileName = fileName + Constants.P7S_FILE_SUFFIX;
		}

		// File outputFile = new File(file.getParentFile(), newFileName);
		File outputFile = new File("/tmp/", newFileName);
		return outputFile;
	}

	// Instance

	public static ConfirmPanel getInstance() {
		return ConfirmPanel.instance;
	}

}
