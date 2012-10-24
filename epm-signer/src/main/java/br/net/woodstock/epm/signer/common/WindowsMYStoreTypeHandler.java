package br.net.woodstock.epm.signer.common;

import java.security.KeyStore;

import javax.swing.JOptionPane;

import br.net.woodstock.rockframework.security.ProviderType;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.impl.JCAStore;

public class WindowsMYStoreTypeHandler implements StoreTypeHandler {

	private static final String	TYPE_NAME	= "Windows MY";

	private static final String	PROVIDER	= ProviderType.SUN_MSCAPI.getType();

	private Store				store;

	public WindowsMYStoreTypeHandler() {
		super();
	}

	@Override
	public void execute() {
		try {
			KeyStore keyStore = KeyStore.getInstance(KeyStoreType.WINDOWS_MY.getType(), ProviderType.SUN_MSCAPI.getType());
			keyStore.load(null, null);
			this.store = new JCAStore(keyStore);
			SignerHolder.getInstance().setStore(this.store);
			SignerHolder.getInstance().onSelectStore();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e, SignerHolder.getInstance().getMessage().getMessage(Constants.LABEL_ERROR), JOptionPane.ERROR_MESSAGE);
			SignerLog.getLogger().debug(e.getMessage(), e);
		}
	}

	@Override
	public String getProvider() {
		return WindowsMYStoreTypeHandler.PROVIDER;
	}

	@Override
	public String toString() {
		return WindowsMYStoreTypeHandler.TYPE_NAME;
	}

}
