package br.net.woodstock.epm.office.oo.impl;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

import br.net.woodstock.epm.office.OfficeLog;
import br.net.woodstock.epm.office.oo.OpenOfficeException;
import br.net.woodstock.epm.office.oo.OpenOfficeServer;

public abstract class AbstractOpenOfficeServer implements OpenOfficeServer {

	private static final String	FILE_PREFIX		= "soffice";

	private static final String	FILE_SUFIX		= ".sh";

	private static final String	CMD_START		= "/bin/sh";

	private static final String	CMD_SEPARATOR	= " \\";

	private Process				process;

	private Thread				thread;

	private OpenOfficeConsole	console;

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
			Thread.sleep(1000);
			this.pipe(this.process.getInputStream(), System.out, "STDOUT > ");
			this.pipe(this.process.getErrorStream(), System.out, "STDERR > ");
			OfficeLog.getLogger().info("Server Started");
		} catch (Exception e) {
			throw new OpenOfficeException(e);
		}
	}

	@Override
	public void stop() {
		OfficeLog.getLogger().info("Starting Server");
		if (this.process != null) {
			this.process.destroy();
			this.process = null;
		}

		if (this.console != null) {
			this.console.stop();
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

	private void pipe(final InputStream source, final PrintStream out, final String name) {
		this.console = new OpenOfficeConsole(source, out, name);
		this.thread = new Thread(this.console);
		// this.thread.setDaemon(true);
		this.thread.start();
	}

	public abstract String[] getCommand();

	public static class OpenOfficeConsole implements Runnable {

		private InputStream	source;

		private PrintStream	out;

		private String		name;

		private boolean		run;

		public OpenOfficeConsole(final InputStream source, final PrintStream out, final String name) {
			super();
			OfficeLog.getLogger().info("Creating Console");
			this.source = source;
			this.out = out;
			this.name = name;
			this.run = true;
		}

		@Override
		public void run() {
			OfficeLog.getLogger().info("Starting Console");
			Scanner scanner = new Scanner(this.source);
			while (this.run) {
				if (scanner.hasNextLine()) {
					String s = scanner.nextLine();
					if (s == null) {
						break;
					}
					this.out.println(this.name + s);
				}
			}
			OfficeLog.getLogger().info("Console Stopped");
		}

		public void stop() {
			this.run = false;
		}

	}

}
