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
import br.net.woodstock.rockframework.security.sign.PKCS7SignatureMode;
import br.net.woodstock.rockframework.security.sign.PKCS7SignatureParameters;
import br.net.woodstock.rockframework.security.sign.SignatureInfo;
import br.net.woodstock.rockframework.security.sign.Signer;
import br.net.woodstock.rockframework.security.sign.impl.BouncyCastlePKCS7Signer;
import br.net.woodstock.rockframework.security.sign.impl.PDFSigner;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.timestamp.TimeStampClient;
import br.net.woodstock.rockframework.security.timestamp.impl.URLTimeStampClient;
import br.net.woodstock.rockframework.utils.ConditionUtils;
import br.net.woodstock.rockframework.utils.FileUtils;
import br.net.woodstock.rockframework.utils.IOUtils;

public class ConfirmPanel extends JPanel {

	private static final long	serialVersionUID	= -174842827285258834L;

	private JButton				btSign;

	public ConfirmPanel() {
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
		this.btSign = new JButton(ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_SIGN));
		this.btSign.setEnabled(false);

		this.add(this.btSign, SwingUtils.getConstraints(line, 0, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE));
	}

	protected void addGUIEvents() {

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
			Alias alias = null;

			if ((ConditionUtils.isNotEmpty(ApplicationHolder.getInstance().getPassword()))) {
				alias = new PasswordAlias(selectedAlias.getName(), new String(ApplicationHolder.getInstance().getPassword()));
			} else {
				alias = new Alias(selectedAlias.getName());
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
			SignatureInfo signatureInfo = new SignatureInfo();
			PKCS7SignatureParameters parameters = new PKCS7SignatureParameters(alias, store, signatureInfo, timeStampClient, mode);
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

			JOptionPane.showMessageDialog(this, ApplicationHolder.getInstance().getMessage().getMessage(Constants.MSG_SUCCESS), ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_INFO), JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex, ApplicationHolder.getInstance().getMessage().getMessage(Constants.LABEL_ERROR), JOptionPane.ERROR_MESSAGE);
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

		File outputFile = new File(file.getParentFile(), newFileName);
		return outputFile;
	}

}
