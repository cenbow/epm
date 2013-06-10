package br.net.woodstock.epm.signer.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import br.net.woodstock.rockframework.security.Identity;
import br.net.woodstock.rockframework.security.cert.CertificateRequest;
import br.net.woodstock.rockframework.security.cert.CertificateResponse;
import br.net.woodstock.rockframework.security.cert.ExtendedKeyUsageType;
import br.net.woodstock.rockframework.security.cert.KeySizeType;
import br.net.woodstock.rockframework.security.cert.KeyUsageType;
import br.net.woodstock.rockframework.security.cert.ext.icpbrasil.DadoPessoa;
import br.net.woodstock.rockframework.security.cert.ext.icpbrasil.PessoaFisicaCertificateExtensionHandler;
import br.net.woodstock.rockframework.security.cert.ext.icpbrasil.TipoFormato;
import br.net.woodstock.rockframework.security.cert.impl.BouncyCastleCertificateGenerator;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.PrivateKeyEntry;
import br.net.woodstock.rockframework.security.store.Store;
import br.net.woodstock.rockframework.security.store.impl.JCAStore;

@RunWith(BlockJUnit4ClassRunner.class)
public class CertificateTest {

	public CertificateTest() {
		super();
	}

	// @Test
	public void testCreateCA() throws Exception {
		CertificateRequest request = new CertificateRequest("Woodstock Tecnologia CA");
		request.setCa(true);
		request.setComment("Woodstock Tecnologia CA");
		request.setEmail("ca@woodstock.net.br");
		request.setKeySize(KeySizeType.KEYSIZE_4K);

		CertificateResponse response = BouncyCastleCertificateGenerator.getInstance().generate(request);
		Identity identity = response.getIdentity();

		Store store = new JCAStore(KeyStoreType.PKCS12);
		store.add(new PrivateKeyEntry(new PasswordAlias("woodstock", "woodstock"), identity));
		store.write(new FileOutputStream("/home/lourival/tmp/cert/woodstock.pfx"), "woodstock");
	}

	@Test
	public void testCreate() throws Exception {
		CertificateRequest request = new CertificateRequest("Lourival Sabino");
		request.setEmail("lourival.sabino.junior@gmail.com");
		// request.withIssuer("Woodstock Tecnologia");
		request.setKeySize(KeySizeType.KEYSIZE_1K);
		request.getKeyUsage().add(KeyUsageType.DIGITAL_SIGNATURE);
		request.getKeyUsage().add(KeyUsageType.NON_REPUDIATION);
		request.getKeyUsage().add(KeyUsageType.KEY_ENCIPHERMENT);
		request.getExtendedKeyUsage().add(ExtendedKeyUsageType.CLIENT_AUTH);
		request.getExtendedKeyUsage().add(ExtendedKeyUsageType.EMAIL_PROTECTION);

		// ICP Brasil
		DadoPessoa dadoPessoa = new DadoPessoa();
		dadoPessoa.setCpf("11111111111");
		dadoPessoa.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse("24/05/1979"));
		dadoPessoa.setEmissorRG("SSP/DF");
		dadoPessoa.setPis("33333333333");
		dadoPessoa.setRg("2222222");

		PessoaFisicaCertificateExtensionHandler extension = new PessoaFisicaCertificateExtensionHandler();

		extension.setTipoFormato(TipoFormato.A3);
		extension.setCei("111111111111");
		extension.setDadoTitular(dadoPessoa);
		extension.setRegistroOAB("DF123456-A");
		extension.setRegistroSINCOR("123456DF");
		extension.setRic("66666666666");
		extension.setTituloEleitor("7777777777777");

		// extensions.process(request);
		request.getExtensionHandlers().add(extension);

		// CA
		FileInputStream inputStream = new FileInputStream("/home/lourival/tmp/cert/woodstock.pfx");
		Store caStore = new JCAStore(KeyStoreType.PKCS12);
		caStore.read(inputStream, "woodstock");
		PrivateKeyEntry entry = (PrivateKeyEntry) caStore.get(new PasswordAlias("woodstock", "woodstock"));
		request.setIssuerIdentity(entry.toIdentity());

		Identity identity = BouncyCastleCertificateGenerator.getInstance().generate(request).getIdentity();

		Store store = new JCAStore(KeyStoreType.PKCS12);
		store.add(new PrivateKeyEntry(new PasswordAlias("lourival", "lourival"), identity));
		store.write(new FileOutputStream("/home/lourival/tmp/cert/lourival-x.pfx"), "lourival");

		FileOutputStream outputStream = new FileOutputStream("/home/lourival/tmp/cert/lourival.cer");
		outputStream.write(identity.getChain()[0].getEncoded());
		outputStream.close();

		// X509Certificate certificate = (X509Certificate) holder.getChain()[0];
		// X500Principal principal = certificate.getSubjectX500Principal();
		// System.out.println(certificate);
		// System.out.println(principal);
		// System.out.println(principal.getName(X500Principal.CANONICAL));
	}
}
