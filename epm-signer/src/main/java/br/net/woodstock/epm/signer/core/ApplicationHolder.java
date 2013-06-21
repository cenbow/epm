package br.net.woodstock.epm.signer.core;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import br.net.woodstock.rockframework.core.utils.Conditions;
import br.net.woodstock.rockframework.core.utils.SystemProperties;
import br.net.woodstock.rockframework.security.Alias;
import br.net.woodstock.rockframework.security.store.Store;

public final class ApplicationHolder {

	private static final String			PREF_STORE_HANDLER	= "STORE_HANDLER";

	private static final String			PREF_STORE			= "STORE";

	private static final String			PREF_ALIAS			= "ALIAS";

	private static final String			PREF_TIMESTAMP_URL	= "TIMESTAMP_URL";

	private static final String			PREF_P7S_DETACHED	= "P7S_DETACHED";

	private static final String			PREF_PDF			= "PDF";

	private static ApplicationHolder	instance			= new ApplicationHolder();

	private Preferences					preferences;

	private Alias						alias;

	private Store						store;

	private StoreTypeHandler			handler;

	private String						password;

	private String						timeStampUrl;

	private boolean						pdf;

	private boolean						p7sDetached;

	private Set<File>					selectedFiles;

	private Set<StoreTypeHandler>		handlers;

	private ApplicationHolder() {
		super();
		this.handlers = new HashSet<StoreTypeHandler>();
		this.handlers.add(new PKCS12StoreTypeHandler());
		if (System.getProperty(SystemProperties.OS_NAME_PROPERTY).startsWith(Constants.WINDOWS_OS_NAME)) {
			this.handlers.add(new WindowsMYStoreTypeHandler());
		}

		this.preferences = Preferences.userNodeForPackage(ApplicationHolder.class);

		String className = this.preferences.get(ApplicationHolder.PREF_STORE_HANDLER, null);
		if (Conditions.isNotEmpty(className)) {
			try {
				Class<?> clazz = Class.forName(className);
				for (StoreTypeHandler sth : this.handlers) {
					if (sth.getClass().equals(clazz)) {
						this.handler = sth;
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				SignerLog.getLogger().debug(e.getMessage(), e);
			}
		}

		this.timeStampUrl = this.preferences.get(ApplicationHolder.PREF_TIMESTAMP_URL, "");
		this.pdf = this.preferences.getBoolean(ApplicationHolder.PREF_PDF, false);
		this.p7sDetached = this.preferences.getBoolean(ApplicationHolder.PREF_P7S_DETACHED, false);
	}

	public void setAlias(final Alias alias) {
		if (alias != null) {
			this.preferences.put(ApplicationHolder.PREF_ALIAS, alias.getName());
		} else {
			this.preferences.remove(ApplicationHolder.PREF_ALIAS);
		}
		this.alias = alias;
	}

	public Alias getAlias() {
		return this.alias;
	}

	public void setStore(final Store store) {
		if (store != null) {
			this.preferences.put(ApplicationHolder.PREF_STORE, store.toString());
		} else {
			this.preferences.remove(ApplicationHolder.PREF_STORE);
		}
		this.store = store;
	}

	public Store getStore() {
		return this.store;
	}

	public StoreTypeHandler getHandler() {
		return this.handler;
	}

	public void setHandler(final StoreTypeHandler handler) {
		if (handler != null) {
			this.preferences.put(ApplicationHolder.PREF_STORE_HANDLER, handler.getClass().getCanonicalName());
		} else {
			this.preferences.remove(ApplicationHolder.PREF_STORE_HANDLER);
		}
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
		if (Conditions.isNotEmpty(timeStampUrl)) {
			this.preferences.put(ApplicationHolder.PREF_TIMESTAMP_URL, timeStampUrl);
		} else {
			this.preferences.remove(ApplicationHolder.PREF_TIMESTAMP_URL);
		}
		this.timeStampUrl = timeStampUrl;
	}

	public boolean isPdf() {
		return this.pdf;
	}

	public void setPdf(final boolean pdf) {
		this.preferences.putBoolean(ApplicationHolder.PREF_PDF, pdf);
		this.pdf = pdf;
	}

	public boolean isP7sDetached() {
		return this.p7sDetached;
	}

	public void setP7sDetached(final boolean p7sDetached) {
		this.preferences.putBoolean(ApplicationHolder.PREF_P7S_DETACHED, p7sDetached);
		this.p7sDetached = p7sDetached;
	}

	public Set<File> getSelectedFiles() {
		return this.selectedFiles;
	}

	public void setSelectedFiles(final Set<File> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}

	public Set<StoreTypeHandler> getHandlers() {
		return this.handlers;
	}

	public void setHandlers(final Set<StoreTypeHandler> handlers) {
		this.handlers = handlers;
	}

	// Preferences
	public void savePreferences() throws BackingStoreException {
		this.preferences.flush();
	}

	// Events
	public void onSelectStore() {
		KeyStorePanel.getInstance().onSelectStore();
	}

	// Instance
	public static ApplicationHolder getInstance() {
		return ApplicationHolder.instance;
	}

}
