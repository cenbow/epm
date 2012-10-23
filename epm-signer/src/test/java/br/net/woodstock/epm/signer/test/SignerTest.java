package br.net.woodstock.epm.signer.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import junit.framework.TestCase;
import br.net.woodstock.rockframework.security.sign.PKCS7SignatureParameters;
import br.net.woodstock.rockframework.security.sign.Signatory;
import br.net.woodstock.rockframework.security.sign.Signature;
import br.net.woodstock.rockframework.security.sign.SignatureInfo;
import br.net.woodstock.rockframework.security.sign.impl.PDFSigner;
import br.net.woodstock.rockframework.security.store.KeyStoreType;
import br.net.woodstock.rockframework.security.store.PasswordAlias;
import br.net.woodstock.rockframework.security.store.impl.JCAStore;
import br.net.woodstock.rockframework.security.timestamp.TimeStamp;
import br.net.woodstock.rockframework.security.timestamp.TimeStampClient;
import br.net.woodstock.rockframework.security.timestamp.impl.URLTimeStampClient;
import br.net.woodstock.rockframework.utils.IOUtils;

public class SignerTest extends TestCase {

	public static final String[]	FREE_TSA	= new String[] { "http://tsa.safelayer.com:8093", "https://tsa.aloaha.com/tsa.asp", "http://dse200.ncipher.com/TSS/HttpTspServer", "http://ca.signfiles.com/TSAServer.aspx" };

	public SignerTest() {
		super();
	}

	public void test7() throws Exception {
		JCAStore store = new JCAStore(KeyStoreType.JKS);
		store.read(new FileInputStream("/home/lourival/tmp/cert/lourival.pfx"), "lourival");

		FileInputStream fileInputStream = new FileInputStream("/home/lourival/Documentos/teste.pdf");

		TimeStampClient timeStampClient = new URLTimeStampClient("http://tsa.safelayer.com:8093");
		PKCS7SignatureParameters signatureParameters = new PKCS7SignatureParameters(new PasswordAlias("lourival", "lourival"), store);
		SignatureInfo signatureInfo = new SignatureInfo();
		signatureInfo.setContactInfo("ConcactInfo");
		signatureInfo.setLocation("Location");
		signatureInfo.setName("Lourival Sabino");
		signatureInfo.setReason("Reason");
		signatureParameters.setTimeStampClient(timeStampClient);
		signatureParameters.setSignatureInfo(signatureInfo);

		PDFSigner signer = new PDFSigner(signatureParameters);

		byte[] signed = signer.sign(IOUtils.toByteArray(fileInputStream));
		FileOutputStream fileOutputStream = new FileOutputStream("/tmp/teste-demo.pdf");
		fileOutputStream.write(signed);

		fileInputStream.close();
		fileOutputStream.close();
	}

	public void xtest8() throws Exception {
		PDFSigner signer = new PDFSigner(null);
		FileInputStream inputStream = new FileInputStream("/tmp/sign2.pdf");
		Signature[] signatures = signer.getSignatures(IOUtils.toByteArray(inputStream));
		for (Signature signature : signatures) {
			System.out.println(signature.getSignatureInfo().getLocation());
			for (Signatory signatory : signature.getSignatories()) {
				System.out.println("\tSubject: " + signatory.getSubject());
				System.out.println("\tIssuer : " + signatory.getIssuer());
			}
			TimeStamp timeStamp = signature.getTimeStamp();
			if (timeStamp != null) {
				System.out.println("Salvando o timeStamp");
				FileOutputStream outputStream = new FileOutputStream("/tmp/teste-demo.pdf.p7m");
				outputStream.write(timeStamp.getEncoded());
				outputStream.close();
			}
			System.out.println("Salvando a assinatura");
			FileOutputStream outputStream = new FileOutputStream("/tmp/teste-demo.pdf.p7s");
			outputStream.write(signature.getEncoded());
			outputStream.close();
		}
		inputStream.close();
	}
}
