package com.example.pki.pkiapplication;

import com.example.pki.pkiapplication.model.*;
import com.example.pki.pkiapplication.util.KeyStoreReader;
import com.example.pki.pkiapplication.util.KeyStoreWriter;
import com.example.pki.pkiapplication.util.RSAKeyGenerator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashSet;

import static com.example.pki.pkiapplication.util.CertificateGenerator.*;

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
	private static RSAKeyGenerator rsaKeyGenerator;
	public static void main(String[] args) {

		context = SpringApplication.run(PkiApplication.class, args);
		keyStoreReader = (KeyStoreReader) context.getBean("keyStoreReader");
		keyStoreWriter = (KeyStoreWriter) context.getBean("keyStoreWriter");
		rsaKeyGenerator = (RSAKeyGenerator) context.getBean("rsaKeyGenerator");

		KeyPair keyPair = rsaKeyGenerator.generateKeys();

		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

		String publicKeyString = Base64.getEncoder().encodeToString(publicKeyBytes);

		CSR certificate = new CSR();
		certificate.setId(1L);
		certificate.setPublicKey(publicKeyString);
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

		Issuer issuer = new Issuer();
		issuer.setPrivateKey(keyPair.getPrivate());
		issuer.setX500name(x500NameBuilderSubject.build());


		keyStoreWriter.loadKeyStore("src/main/resources/static/example.jks",  "password".toCharArray());

        keyStoreWriter.write("cert1", issuer.getPrivateKey(), "password".toCharArray(), generateCertificate(issuer, certificate));
		keyStoreWriter.saveKeyStore("src/main/resources/static/example.jks",  "password".toCharArray());


		java.security.cert.Certificate loadedCertificate = keyStoreReader.readCertificate("src/main/resources/static/example.jks", "password", "cert1");

		System.out.println(loadedCertificate);
	}
}
