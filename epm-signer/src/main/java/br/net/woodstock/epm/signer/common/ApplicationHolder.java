package br.net.woodstock.epm.signer.common;

import java.io.File;
import java.util.Set;

import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.store.Store;

public final class ApplicationHolder {

	private static ApplicationHolder	instance	= new ApplicationHolder();

	private SignerMessage				message		= SignerMessage.getInstance();

	private Alias						alias;

	private Store						store;

	private StoreTypeHandler			handler;

	private String						password;

	private String						timeStampUrl;

	private boolean						pdf;

	private boolean						p7sDetached;

	private Set<File>					selectedFiles;

	private ApplicationHolder() {
		super();
	}

	public String getMessage(final String key) {
		return this.message.getMessage(key);
	}

	public String getMessage(final String key, final Object... args) {
		return this.message.getMessage(key, args);
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

	public StoreTypeHandler getHandler() {
		return this.handler;
	}

	public void setHandler(final StoreTypeHandler handler) {
		this.handler = handler;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getTimeStampUrl() {
		return this.timeStampUrl;
	}

	public void setTimeStampUrl(final String timeStampUrl) {
		this.timeStampUrl = timeStampUrl;
	}

	public boolean isPdf() {
		return this.pdf;
	}

	public void setPdf(final boolean pdf) {
		this.pdf = pdf;
	}

	public boolean isP7sDetached() {
		return this.p7sDetached;
	}

	public void setP7sDetached(final boolean p7sDetached) {
		this.p7sDetached = p7sDetached;
	}

	public Set<File> getSelectedFiles() {
		return this.selectedFiles;
	}

	public void setSelectedFiles(final Set<File> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}

	// Evenets
	public void onSelectStore() {
		KeyStorePanel.getInstance().onSelectStore();
	}

	// Instance
	public static ApplicationHolder getInstance() {
		return ApplicationHolder.instance;
	}

}
