package br.net.woodstock.epm.office.oo.impl;

import java.io.File;
import java.io.PrintWriter;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeConnection;
import br.net.woodstock.epm.office.oo.OpenOfficeException;
import br.net.woodstock.epm.office.oo.OpenOfficeServer;

public abstract class AbstractOpenOfficeServer implements OpenOfficeServer {

	private static final String		FILE_PREFIX		= "soffice";

	private static final String		FILE_SUFIX		= ".sh";

	private static final String		CMD_START		= "/bin/sh";

	private static final String		CMD_SEPARATOR	= " \\";

	private static final int		MAX_TRIES		= 10;

	private Process					process;

	private OpenOfficeConnection	connection;

	public AbstractOpenOfficeServer() {
		super();
	}

	@Override
	public void start() {
		try {
			OfficeLog.getLogger().info("Starting Server");

			File file = File.createTempFile(AbstractOpenOfficeServer.FILE_PREFIX, AbstractOpenOfficeServer.FILE_SUFIX);
			PrintWriter writer = new PrintWriter(file);
			String[] command = this.getCommand();
			for (String s : command) {
				writer.println(s + AbstractOpenOfficeServer.CMD_SEPARATOR);
			}
			writer.close();
			file.setExecutable(true);

			OfficeLog.getLogger().info("Server Script " + file.getAbsolutePath());

			ProcessBuilder builder = new ProcessBuilder(AbstractOpenOfficeServer.CMD_START, file.getAbsolutePath());
			this.process = builder.start();
			OfficeLog.getLogger().info("Server Started");

			for (int i = 0; i < AbstractOpenOfficeServer.MAX_TRIES; i++) {
				try {
					Thread.sleep(500);
					OfficeLog.getLogger().info("Starting Connection(" + i + ")");
					this.connection = this.newConnection();
					this.connection.connect();
					break;
				} catch (Exception e) {
					OfficeLog.getLogger().debug(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			throw new OpenOfficeException(e);
		}
	}

	@Override
	public void stop() {
		try {
			OpenOfficeConnection connection = this.getConnection();
			connection.close();
		} catch (Exception e) {
			OfficeLog.getLogger().info(e.getMessage(), e);
		}

		OfficeLog.getLogger().info("Stopping Server");
		if (this.process != null) {
			this.process.destroy();
			this.process = null;
		}
		OfficeLog.getLogger().info("Server Stoped");
	}

	@Override
	public boolean isRunning() {
		if (this.process != null) {
			return true;
		}
		return false;
	}

	@Override
	public final OpenOfficeConnection getConnection() {
		if (this.connection == null) {
			synchronized (this.connection) {
				this.connection = this.newConnection();
			}
		}
		return this.connection;
	}

	public abstract String[] getCommand();

	protected abstract OpenOfficeConnection newConnection();

}
