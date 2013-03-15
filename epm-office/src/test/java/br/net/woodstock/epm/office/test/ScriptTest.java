package br.net.woodstock.epm.office.test;

import java.io.IOException;

public final class ScriptTest {

	private ScriptTest() {
		super();
	}

	public static void main(final String[] args) {
		ProcessBuilder builder = new ProcessBuilder("/bin/sh", "/tmp/soffice.sh");
		try {
			Process process = builder.start();
			System.out.println(process);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
