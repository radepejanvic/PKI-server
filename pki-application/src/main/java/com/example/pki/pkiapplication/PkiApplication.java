package com.example.pki.pkiapplication;

import com.example.pki.pkiapplication.model.*;
import com.example.pki.pkiapplication.model.enums.CSRStatus;
import com.example.pki.pkiapplication.model.enums.CertificateType;
import com.example.pki.pkiapplication.service.impl.KeyStoringServiceImpl;
import com.example.pki.pkiapplication.util.*;
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
import java.util.Base64;

@SpringBootApplication
public class PkiApplication {

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().buildValidatorFactory();
		return validatorFactory.getValidator();
	}
	public static void main(String[] args) {

		SpringApplication.run(PkiApplication.class, args);

	}
}
