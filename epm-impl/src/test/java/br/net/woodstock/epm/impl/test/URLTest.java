package br.net.woodstock.epm.impl.test;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class URLTest {

	public static void main(final String[] args) {
		try {
			System.setProperty("http.proxyHost", "proxy.tse.jus.br");
			System.setProperty("http.proxyPort", "8080");
			System.setProperty("http.proxyUser", "lourival.junior");
			System.setProperty("http.proxyPassword", "M3t4ll1c@");
			System.setProperty("sun.net.client.defaultConnectTimeout", "15000");
			System.setProperty("sun.net.client.defaultReadTimeout", "15000");
			URL url = new URL("https://www.cnj.jus.br/testeReceitaFederal/wsdl/proxyReceitaCNPJ.wsdl");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream();
			Scanner scanner = new Scanner(inputStream);
			while (scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
