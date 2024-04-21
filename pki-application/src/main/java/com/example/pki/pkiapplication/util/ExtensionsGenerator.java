package com.example.pki.pkiapplication.util;

import com.example.pki.pkiapplication.dto.CSRDTO;
import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Issuer;
import com.example.pki.pkiapplication.model.enums.CertificateType;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509v3CertificateBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;


public class ExtensionsGenerator {
    public static void addExtensions(X509v3CertificateBuilder certificateBuilder, CSR csr, Issuer issuer) throws Exception {

        System.out.println("U ekstenziji: " + csr);
        certificateBuilder.addExtension(Extension.basicConstraints, true, generateBasicConstraints(csr));
        certificateBuilder.addExtension(Extension.subjectKeyIdentifier, false, generateSubjectKeyIdentifier(csr));
        certificateBuilder.addExtension(Extension.keyUsage, true, generateKeyUsage(csr));

        if(!csr.getTemplate().equals(CertificateType.SS)){
            AuthorityKeyIdentifier aki = new AuthorityKeyIdentifier(SubjectPublicKeyInfo.getInstance(issuer.getPublicKey()));
            certificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, aki);
        }
        if(csr.getTemplate().equals(CertificateType.EE)){
            GeneralName san = new GeneralName(GeneralName.dNSName, csr.getDomainName());
            GeneralNames sanNames = new GeneralNames(san);
            certificateBuilder.addExtension(org.bouncycastle.asn1.x509.Extension.subjectAlternativeName, false, sanNames);
        }
    }

    public static byte[] generateSubjectKeyIdentifier(CSR csr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyDecoder keyDecoder = new KeyDecoder();
        return generateSubjectKeyIdentifier(keyDecoder.decodePublic(csr.getPublicKey()).getEncoded());
    }
    public static BasicConstraints generateBasicConstraints(CSR csr){
        BasicConstraints basicConstraints;
        if(csr.getTemplate().equals(CertificateType.CA) || csr.getTemplate().equals(CertificateType.SS)){
            basicConstraints = new BasicConstraints(true);
        }else{
            basicConstraints = new BasicConstraints(false);
        }
        return basicConstraints;
    }

    public static KeyUsage generateKeyUsage(CSR csr){
        boolean[] keyUsageArray = new boolean[9];
        for (com.example.pki.pkiapplication.model.enums.KeyUsage keyUsage : csr.getKeyUsages()) {
            keyUsageArray[keyUsage.ordinal()] = true;
        }
        int keyUsageValue = 0;
        for (int i = 0; i < keyUsageArray.length; i++) {
            if (keyUsageArray[i]) {
                keyUsageValue |= (1 << i);
            }
        }
        return  new org.bouncycastle.asn1.x509.KeyUsage(keyUsageValue);
    }
    public static byte[] generateSubjectKeyIdentifier(byte[] publicKey)
            throws  NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(publicKey);
        return Arrays.copyOfRange(hash, 0, 20);
    }

}
