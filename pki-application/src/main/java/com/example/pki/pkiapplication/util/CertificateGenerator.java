package com.example.pki.pkiapplication.util;

import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.enums.CertificateType;
import com.example.pki.pkiapplication.model.Issuer;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.spec.InvalidKeySpecException;
import java.util.Date;

@Component
public class CertificateGenerator {

    @Autowired
    private KeyDecoder keyDecoder;

    public CertificateGenerator() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate generateCertificate(Issuer issuer, CSR csr, PublicKey publicKey) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuer.getPrivateKey());

            Long currentMillis = System.currentTimeMillis();

            PublicKey subjectPublicKey = publicKey != null ? publicKey : keyDecoder.decodePublic(csr.getPublicKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer.getX500name(),
                    BigInteger.valueOf(currentMillis),
                    new Date(currentMillis),
                    new Date(getExpiresOn(currentMillis, csr.getTemplate())),
                    getX500Name(csr),
                    subjectPublicKey);

            System.out.println("Pre ekstenzija: " + csr);
            ExtensionsGenerator.addExtensions(certGen, csr, issuer);

            System.out.println("Posle ekstenzija 1: " + csr);

            X509CertificateHolder certHolder = certGen.build(contentSigner);

            System.out.println("Posle ekstenzija 2: " + csr);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            System.out.println("Posle ekstenzija 3: " + csr);

            return certConverter.getCertificate(certHolder);

        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public X509Certificate generateRootCertificate(CSR csr, KeyPair keyPair) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(keyPair.getPrivate());

            Long currentMillis = System.currentTimeMillis();

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(getX500Name(csr),
                    BigInteger.valueOf(currentMillis),
                    new Date(currentMillis),
                    new Date(getExpiresOn(currentMillis, csr.getTemplate())),
                    getX500Name(csr),
                    keyPair.getPublic());

            X509CertificateHolder certHolder = certGen.build(contentSigner);

            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");

            return certConverter.getCertificate(certHolder);

        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }


    public X500Name getX500Name(CSR csr){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, csr.getCommonName());
        builder.addRDN(BCStyle.O, csr.getOrganization());
        builder.addRDN(BCStyle.C, csr.getCountry());
        builder.addRDN(BCStyle.E, csr.getEmail());
        return builder.build();
    }

    public Long getExpiresOn(Long issuedOn, CertificateType type){
        Long ret = issuedOn;
        switch (type) {
            case CertificateType.SS:
                ret += 315576000000L;
                break;
            case CertificateType.CA:
                ret +=  126230400000L;
                break;
            case CertificateType.EE:
                ret +=  15778800000L;
                break;
        }
        return ret;
    }

}
