package com.example.pki.pkiapplication;

import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.CSRStatus;
import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.model.CertificateType;
import com.example.pki.pkiapplication.util.KeyStoreReader;
import com.example.pki.pkiapplication.util.KeyStoreWriter;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;

import static com.example.pki.pkiapplication.util.CertificateGenerator.generateCertificate;
import static com.example.pki.pkiapplication.util.CertificateGenerator.getPrK;

@SpringBootApplication
public class PkiApplication {

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().buildValidatorFactory();
		return validatorFactory.getValidator();
	}

	private static KeyStoreReader keyStoreReader;

	private static KeyStoreWriter keyStoreWriter;

	private static ApplicationContext context;
	public static void main(String[] args) {

		context = SpringApplication.run(PkiApplication.class, args);
		keyStoreReader = (KeyStoreReader) context.getBean("keyStoreReader");
		keyStoreWriter = (KeyStoreWriter) context.getBean("keyStoreWriter");

		CSR certificate = new CSR();
		certificate.setId(1L);
		certificate.setPublicKey("-----BEGIN RSA PUBLIC KEY-----\n" +
				"MEgCQQCo9+BpMRYQ/dL3DS2CyJxRF+j6ctbT3/Qp84+KeFhnii7NT7fELilKUSnx\n" +
				"S30WAvQCCo2yU1orfgqr41mM70MBAgMBAAE=\n" +
				"-----END RSA PUBLIC KEY-----");
		certificate.setCommonName("Dusan Djordjevic");
		certificate.setOrganization("SIIT");
		certificate.setCountry("Serbia");
		certificate.setEmail("djordjevicdusan24@gmail.com");
		certificate.setTemplate(CertificateType.EE);
		certificate.setStatus(CSRStatus.APPROVED);

		X500NameBuilder x500NameBuilderSubject = new X500NameBuilder(BCStyle.INSTANCE);
		x500NameBuilderSubject.addRDN(BCStyle.CN, "Rade Pejanovic");
		x500NameBuilderSubject.addRDN(BCStyle.O, "FTN");
		x500NameBuilderSubject.addRDN(BCStyle.C, "Serbia");
		x500NameBuilderSubject.addRDN(BCStyle.E, "radefaks@gmail.com");

		String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
				"MIIBOgIBAAJBAKj34GkxFhD90vcNLYLInFEX6Ppy1tPf9Cnzj4p4WGeKLs1Pt8Qu\n" +
				"KUpRKfFLfRYC9AIKjbJTWit+CqvjWYzvQwECAwEAAQJAIJLixBy2qpFoS4DSmoEm\n" +
				"o3qGy0t6z09AIJtH+5OeRV1be+N4cDYJKffGzDa88vQENZiRm0GRq6a+HPGQMd2k\n" +
				"TQIhAKMSvzIBnni7ot/OSie2TmJLY4SwTQAevXysE2RbFDYdAiEBCUEaRQnMnbp7\n" +
				"9mxDXDf6AU0cN/RPBjb9qSHDcWZHGzUCIG2Es59z8ugGrDY+pxLQnwfotadxd+Uy\n" +
				"v/Ow5T0q5gIJAiEAyS4RaI9YG8EWx/2w0T67ZUVAw8eOMB6BIUg0Xcu+3okCIBOs\n" +
				"/5OiPgoTdSy7bcF9IGpSE8ZgGKzgYQVZeN97YE00\n" +
				"-----END RSA PRIVATE KEY-----";


		keyStoreWriter.loadKeyStore("src/main/resources/static/example.jks",  "password".toCharArray());
		PrivateKey pk;
		try {
			pk = getPrK(privateKey);
		} catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        keyStoreWriter.write("cert1", pk, "password".toCharArray(), generateCertificate(x500NameBuilderSubject.build(), pk ,certificate));
		keyStoreWriter.saveKeyStore("src/main/resources/static/example.jks",  "password".toCharArray());


		java.security.cert.Certificate loadedCertificate = keyStoreReader.readCertificate("src/main/resources/static/example.jks", "password", "cert1");

		System.out.println(loadedCertificate);
	}
}
