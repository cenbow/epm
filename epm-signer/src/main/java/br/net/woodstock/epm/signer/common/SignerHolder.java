package br.net.woodstock.epm.signer.common;

import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.store.Store;

public final class SignerHolder {

	private static SignerHolder	instance	= new SignerHolder();

	private SignerMessage		message		= SignerMessage.getInstance();

	private Alias				alias;

	private Store				store;

	private SignerPanel			panel;

	private StoreTypeHandler	handler;

	private SignerHolder() {
		super();
	}

	public SignerMessage getMessage() {
		return this.message;
	}

	public void setAlias(final Alias alias) {
		this.alias = alias;
	}

	public Alias getAlias() {
		return this.alias;
	}

	public void setStore(final Store store) {
		this.store = store;
	}

	public Store getStore() {
		return this.store;
	}

	public SignerPanel getPanel() {
		return this.panel;
	}

	public void setPanel(final SignerPanel panel) {
		this.panel = panel;
	}

	public StoreTypeHandler getHandler() {
		return this.handler;
	}

	public void setHandler(final StoreTypeHandler handler) {
		this.handler = handler;
	}

	// Events
	public void onSelectStore() {
		this.panel.onSelectStore();
	}

	// Instance
	public static SignerHolder getInstance() {
		return SignerHolder.instance;
	}

}
