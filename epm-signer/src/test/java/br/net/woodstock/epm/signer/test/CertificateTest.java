package br.net.woodstock.epm.signer.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.rockframework.security.cert.CertificateRequest;
import br.net.woodstock.rockframework.security.cert.ExtendedKeyUsageType;
import br.net.woodstock.rockframework.security.cert.KeyUsageType;
import br.net.woodstock.rockframework.security.cert.PrivateKeyHolder;
import br.net.woodstock.rockframework.security.cert.ext.icpbrasil.DadoPessoa;
import br.net.woodstock.rockframework.security.cert.ext.icpbrasil.PessoaFisicaCertificateRequest;
import br.net.woodstock.rockframework.security.cert.ext.icpbrasil.TipoFormato;
import br.net.woodstock.rockframework.security.cert.impl.BouncyCastleCertificateGenerator;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.PrivateKeyEntry;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.impl.JCAStore;
import br.net.woodstock.rockframework.util.DateBuilder;

@RunWith(BlockJUnit4ClassRunner.class)
public class CertificateTest {

	public CertificateTest() {
		super();
	}

	// @Test
	public void testCreateCA() throws Exception {
		CertificateRequest request = new CertificateRequest("Woodstock Tecnologia CA");
		request.withCa(true);
		request.withComment("Woodstock Tecnologia CA");
		request.withEmail("ca@woodstock.net.br");
		request.withKeySize(4096);

		PrivateKeyHolder holder = BouncyCastleCertificateGenerator.getInstance().generate(request);

		Store store = new JCAStore(KeyStoreType.PKCS12);
		store.add(new PrivateKeyEntry(new PasswordAlias("woodstock", "woodstock"), holder.getPrivateKey(), holder.getChain()));
		store.write(new FileOutputStream("/home/lourival/tmp/cert/woodstock.pfx"), "woodstock");
	}

	@Test
	public void testCreate() throws Exception {
		PessoaFisicaCertificateRequest request = new PessoaFisicaCertificateRequest("Lourival Sabino");
		request.withEmail("lourival.sabino.junior@gmail.com");
		// request.withIssuer("Woodstock Tecnologia");
		request.withKeySize(1024);
		request.withKeyUsage(KeyUsageType.DIGITAL_SIGNATURE, KeyUsageType.NON_REPUDIATION, KeyUsageType.KEY_ENCIPHERMENT);
		request.withExtendedKeyUsage(ExtendedKeyUsageType.CLIENT_AUTH, ExtendedKeyUsageType.EMAIL_PROTECTION);

		// ICP Brasil
		DadoPessoa dadoPessoa = new DadoPessoa();
		dadoPessoa.setCpf("11111111111");
		dadoPessoa.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse("24/05/1979"));
		dadoPessoa.setEmissorRG("SSP/DF");
		dadoPessoa.setPis("33333333333");
		dadoPessoa.setRg("2222222");

		request.withTipoFormato(TipoFormato.A3);
		request.withCei("111111111111");
		request.withDadoTitular(dadoPessoa);
		request.withRegistroOAB("DF123456-A");
		request.withRegistroSINCOR("123456DF");
		request.withRic("66666666666");
		request.withTituloEleitor("7777777777777");

		DateBuilder builder = new DateBuilder();
		request.withNotBefore(builder.removeDays(1).getDate());
		request.withNotAfter(builder.addYears(1).getDate());

		// CA
		FileInputStream inputStream = new FileInputStream("/home/lourival/tmp/cert/woodstock.pfx");
		Store caStore = new JCAStore(KeyStoreType.PKCS12);
		caStore.read(inputStream, "woodstock");
		PrivateKeyEntry entry = (PrivateKeyEntry) caStore.get(new PasswordAlias("woodstock", "woodstock"));
		request.withIssuerKeyHolder(new PrivateKeyHolder(entry.getValue(), entry.getChain()));

		PrivateKeyHolder holder = BouncyCastleCertificateGenerator.getInstance().generate(request);

		Store store = new JCAStore(KeyStoreType.PKCS12);
		store.add(new PrivateKeyEntry(new PasswordAlias("lourival", "lourival"), holder.getPrivateKey(), holder.getChain()));
		store.write(new FileOutputStream("/tmp/lourival.pfx"), "lourival");

		// FileOutputStream outputStream = new FileOutputStream("/home/lourival/tmp/cert/lourival.cer");
		// outputStream.write(holder.getChain()[0].getEncoded());
		// outputStream.close();

		// X509Certificate certificate = (X509Certificate) holder.getChain()[0];
		// X500Principal principal = certificate.getSubjectX500Principal();
		// System.out.println(certificate);
		// System.out.println(principal);
		// System.out.println(principal.getName(X500Principal.CANONICAL));
	}
}
