package br.net.woodstock.epm.office.oo.impl;

import java.io.File;
import java.io.PrintWriter;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeCallback;
import br.net.woodstock.epm.office.oo.OpenOfficeConfig;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeException;
import br.net.woodstock.epm.office.oo.OpenOfficeManager;

public class SynchronizedOpenOfficeManager implements OpenOfficeManager {

	private static final String	FILE_PREFIX			= "soffice";

	private static final String	FILE_SUFIX			= ".sh";

	private static final String	CMD_START			= "/bin/sh";

	private static final String	CMD_STOP			= "killall -9 soffice.bin";

	private static final String	CMD_SEPARATOR		= " \\";

	private static final int	MAX_PROCESS_TRIES	= 3;

	private static final int	MAX_CONNECT_TRIES	= 10;

	private OpenOfficeConfig	config;

	private Process				process;

	public SynchronizedOpenOfficeManager(final OpenOfficeConfig config) {
		super();
		this.config = config;
	}

	public void start() {
		try {
			OfficeLog.getLogger().info("Starting Server");

			File file = File.createTempFile(SynchronizedOpenOfficeManager.FILE_PREFIX, SynchronizedOpenOfficeManager.FILE_SUFIX);
			PrintWriter writer = new PrintWriter(file);
			String[] command = this.config.getStartupCommand();
			for (String s : command) {
				writer.println(s + SynchronizedOpenOfficeManager.CMD_SEPARATOR);
			}
			writer.close();
			file.setExecutable(true);

			OfficeLog.getLogger().info("Server Script " + file.getAbsolutePath());

			ProcessBuilder builder = new ProcessBuilder(SynchronizedOpenOfficeManager.CMD_START, file.getAbsolutePath());
			this.process = builder.start();

		} catch (Exception e) {
			throw new OpenOfficeException(e);
		}
	}

	public void stop() {
		DefaultOpenOfficeConnection connection = new DefaultOpenOfficeConnection(this.config.getConnectionUrl());
		try {
			connection.connect();
			connection.shutdownDesktop();
			connection.close();
		} catch (Exception e) {
			OfficeLog.getLogger().debug(e.getMessage(), e);
		}

		if (this.process != null) {
			try {
				OfficeLog.getLogger().info("Stopping process");
				this.process.destroy();
				this.process = null;
				OfficeLog.getLogger().info("Process stoped");
			} catch (Exception e) {
				OfficeLog.getLogger().debug(e.getMessage(), e);
			}
		}

		try {
			ProcessBuilder builder = new ProcessBuilder(SynchronizedOpenOfficeManager.CMD_STOP);
			builder.start();
		} catch (Exception e) {
			OfficeLog.getLogger().debug(e.getMessage(), e);
		}

	}

	public void restart() {
		this.stop();
		this.start();
	}

	@Override
	public synchronized <T> T execute(final OpenOfficeCallback callback) {
		OpenOfficeConnection connection = null;
		try {
			OfficeLog.getLogger().info("Executing " + callback.getClass().getName());

			long l = System.currentTimeMillis();

			connection = this.getConnection();

			T t = callback.doInConnection(connection);

			l = System.currentTimeMillis() - l;
			OfficeLog.getLogger().info("Executed in " + l + "ms");

			return t;
		} catch (Exception e) {
			OfficeLog.getLogger().error(e.getMessage(), e);
			throw new OpenOfficeException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					OfficeLog.getLogger().debug(e.getMessage(), e);
				}
			}
		}
	}

	private OpenOfficeConnection getConnection() {
		try {
			if (this.process == null) {
				this.start();
			}

			OpenOfficeConnection connection = null;
			outer: for (int ip = 0; ip < SynchronizedOpenOfficeManager.MAX_PROCESS_TRIES; ip++) {
				for (int ic = 0; ic < SynchronizedOpenOfficeManager.MAX_CONNECT_TRIES; ic++) {
					try {
						Thread.sleep(500);
						OfficeLog.getLogger().debug("Starting Connection(" + ic + ")");
						connection = new DefaultOpenOfficeConnection(this.config.getConnectionUrl());
						connection.connect();
						break outer;
					} catch (Exception e) {
						connection = null;
						OfficeLog.getLogger().debug(e.getMessage(), e);
					}
				}

				if (connection == null) {
					this.restart();
				}
			}

			if (connection == null) {
				throw new OpenOfficeException("Cold not connect to server");
			}
			return connection;
		} catch (Exception e) {
			throw new OpenOfficeException(e);
		}
	}

}
