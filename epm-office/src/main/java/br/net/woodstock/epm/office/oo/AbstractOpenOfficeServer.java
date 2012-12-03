package br.net.woodstock.epm.office.oo;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public abstract class AbstractOpenOfficeServer {

	private Process				process;

	private Thread				thread;

	private OpenOfficeConsole	console;

	public AbstractOpenOfficeServer() {
		super();
	}

	public void start() throws IOException {
		Runtime runtime = Runtime.getRuntime();
		this.process = runtime.exec(this.getCommand());
		this.pipe(this.process.getInputStream(), System.out, "STDOUT > ");
		this.pipe(this.process.getErrorStream(), System.out, "STDERR > ");
	}

	public void stop() {
		if (this.process != null) {
			this.process.destroy();
			this.process = null;
		}

		if (this.console != null) {
			this.console.stop();
		}
	}

	private void pipe(final InputStream inputStream, final PrintStream out, final String name) {
		this.console = new OpenOfficeConsole(inputStream, out, name);
		this.thread = new Thread(this.console);
		this.thread.start();
	}

	public abstract String[] getCommand();

	public static class OpenOfficeConsole implements Runnable {

		private InputStream	inputStream;

		private PrintStream	out;

		private String		name;

		private boolean		run;

		public OpenOfficeConsole(final InputStream inputStream, final PrintStream out, final String name) {
			super();
			this.inputStream = inputStream;
			this.out = out;
			this.name = name;
			this.run = true;
		}

		@Override
		public void run() {
			Scanner scanner = new Scanner(this.inputStream);
			while (this.run) {
				if (scanner.hasNextLine()) {
					String s = scanner.nextLine();
					if (s == null) {
						break;
					}
					this.out.println(this.name + s);
				}
			}
		}

		public void stop() {
			this.run = false;
		}

	}

}
