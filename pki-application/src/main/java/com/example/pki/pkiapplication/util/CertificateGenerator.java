package com.example.pki.pkiapplication.util;

import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.model.CertificateType;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.X509Name;
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
import org.springframework.stereotype.Component;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Component
public class CertificateGenerator {
    public CertificateGenerator() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static X509Certificate generateCertificate(X500Name x500NameIssuer, PrivateKey issuerPK, CSR csr) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuerPK);

            X500NameBuilder x500NameBuilderSubject = new X500NameBuilder(BCStyle.INSTANCE);
            x500NameBuilderSubject.addRDN(BCStyle.CN, csr.getCommonName());
            x500NameBuilderSubject.addRDN(BCStyle.O, csr.getOrganization());
            x500NameBuilderSubject.addRDN(BCStyle.C, csr.getCountry());
            x500NameBuilderSubject.addRDN(BCStyle.E, csr.getEmail());


            Long currentMillis = System.currentTimeMillis();

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(x500NameIssuer,
                    BigInteger.valueOf(currentMillis),
                    new Date(currentMillis),
                    new Date(getExpiresOn(currentMillis, csr.getTemplate())),
                    x500NameBuilderSubject.build(),
                    getPbKey(csr.getPublicKey()));

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
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Long getExpiresOn(Long issuedOn, CertificateType type){
        Long ret = 0L;
        switch (type) {
            case CertificateType.SS:
                ret = 315576000000L;
                break;
            case CertificateType.CA:
                ret =  126230400000L;
            case CertificateType.EE:
                ret =  15778800000L;
                break;
        }
        return ret;
    }

    public static PublicKey getPbKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyPEM = publicKey
                .replace("-----BEGIN RSA PUBLIC KEY-----", "")
                .replace("-----END RSA PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getPrK(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyPEM = privateKey
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpec);
    }

}
