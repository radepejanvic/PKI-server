package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.model.Issuer;
import com.example.pki.pkiapplication.util.CertificateGenerator;
import com.example.pki.pkiapplication.util.KeyEncoder;
import com.example.pki.pkiapplication.util.KeyGenerator;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
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

    @Autowired
    private KeyEncoder keyEncoder;

    public Certificate generateCertificate(CSR csr) {
        return switch (csr.getTemplate()) {
            case SS -> generateRoot(csr);
            case CA -> generateCA(csr);
            case EE -> generateEE(csr);
        };
    }


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
        csr.setPublicKey(keyEncoder.encodePublicKey(keyPair.getPublic()));
        X509Certificate certificate = certificateGenerator.generateCertificate(issuer, csr, keyPair.getPublic());

        System.out.println(certificate);

        keyStoringService.write(csr.getSubjectAlias(), certificate, keyPair.getPrivate());

        System.out.println(keyStoringService.read(csr.getEmail()));
        return convert(certificate);
    }

    public Certificate generateRoot(CSR csr) {

        KeyPair keyPair = keyGenerator.generateKeys();
        csr.setPublicKey(keyEncoder.encodePublicKey(keyPair.getPublic()));
        X509Certificate certificate = certificateGenerator.generateRootCertificate(csr, keyPair);

        keyStoringService.write(csr.getSubjectAlias(), certificate, keyPair.getPrivate());

        System.out.println(keyStoringService.read(csr.getEmail()));
        return convert(certificate);
    }

    public Issuer generateIssuer(String alias) {
        Issuer issuer = new Issuer();

        X509Certificate certificate = keyStoringService.read(alias);
        X500Principal x500Principal = certificate.getIssuerX500Principal();
        issuer.setX500name(new X500Name(x500Principal.getName()));
        issuer.setPrivateKey(keyStoringService.readPrivateKey(alias));
        issuer.setPublicKey(certificate.getPublicKey());

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
