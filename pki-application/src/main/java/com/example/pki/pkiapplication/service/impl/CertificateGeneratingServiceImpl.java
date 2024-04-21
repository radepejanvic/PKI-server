package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.model.Issuer;
import com.example.pki.pkiapplication.util.CertificateGenerator;
import com.example.pki.pkiapplication.util.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

@Service
public class CertificateGeneratingServiceImpl {

    @Autowired
    private KeyStoringServiceImpl keyStoringService;

    @Autowired
    private CertificateGenerator certificateGenerator;

    @Autowired
    private KeyGenerator keyGenerator;

    public Certificate generateEE(CSR csr) {

        Issuer issuer = generateIssuer(csr.getIssuerAlias());
        X509Certificate certificate = certificateGenerator.generateCertificate(issuer, csr, null);

        keyStoringService.write(csr.getSubjectAlias(), certificate, null);

        System.out.println(keyStoringService.read(csr.getEmail()));

        return convert(certificate);
    }

    public Certificate generateCA(CSR csr) {

        Issuer issuer = generateIssuer(csr.getIssuerAlias());
        KeyPair keyPair = keyGenerator.generateKeys();
        X509Certificate certificate = certificateGenerator.generateCertificate(issuer, csr, keyPair.getPublic());

        keyStoringService.write(csr.getSubjectAlias(), certificate, keyPair.getPrivate());

        System.out.println(keyStoringService.read(csr.getEmail()));
        return convert(certificate);
    }

    public Certificate generateRoot(CSR csr) {

        KeyPair keyPair = keyGenerator.generateKeys();
        X509Certificate certificate = certificateGenerator.generateRootCertificate(csr, keyPair);

        keyStoringService.write(csr.getSubjectAlias(), certificate, keyPair.getPrivate());

        System.out.println(keyStoringService.read(csr.getEmail()));
        return convert(certificate);
    }

    public Issuer generateIssuer(String alias) {
        Issuer issuer = new Issuer();

        issuer.setX500name(keyStoringService.readIssuerX500Name(alias));
        issuer.setPrivateKey(keyStoringService.readPrivateKey(alias));

        return issuer;
    }

    public Certificate convert(X509Certificate x509certificate) {
        Certificate certificate = new Certificate();

        certificate.setSerialNumber(x509certificate.getSerialNumber());
        certificate.setSubject(x509certificate.getSubjectX500Principal().toString());
        certificate.setIssuedOn(x509certificate.getNotBefore().getTime());
        certificate.setExpiresOn(x509certificate.getNotAfter().getTime());
//        certificate.setValid(x509certificate.get);

        return certificate;
    }



}