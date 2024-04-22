package com.example.pki.pkiapplication.service.impl;


import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.service.CertificateService;
import com.example.pki.pkiapplication.util.KeyStoreDeleter;
import com.example.pki.pkiapplication.util.KeyStoreReader;
import com.example.pki.pkiapplication.util.KeyStoreWriter;
import com.example.pki.pkiapplication.util.PemKeyStore;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KeyStoringServiceImpl {

    // TODO: Rename the service to something more suitable.

    @Value(value="${keys}")
    private String privateKeyStore;

    @Value(value="${jks.path}")
    private String certStore;

    @Value(value = "${jks.password}")
    private String certStorePass;

    @Autowired
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    private KeyStoreReader keyStoreReader;

    @Autowired
    private KeyStoreDeleter keyStoreDeleter;

    @Autowired
    private PemKeyStore pemKeyStore;

    @Autowired
    private CertificateService certificateService;

    // TODO: Depending on the certificate type create new .jks file.
    // TODO: Expand read and write functions to go over multiple .jks files when implemented.

    public void write(String alias, X509Certificate cert, PrivateKey privateKey) {

        keyStoreWriter.loadKeyStore(certStore, certStorePass.toCharArray());
        keyStoreWriter.write(alias, cert);
        keyStoreWriter.saveKeyStore(certStore, certStorePass.toCharArray());

        if (privateKey != null) {
            String filename = String.format("%s/%s.pem", privateKeyStore, alias);
            pemKeyStore.write(filename, privateKey);
        }
    }

    public X509Certificate read(String alias) {
        return keyStoreReader.readCertificate(certStore, certStorePass, alias);
    }

    public PrivateKey readPrivateKey(String alias) {
        String filename = String.format("%s/%s.pem", privateKeyStore, alias);
        return pemKeyStore.read(filename);
    }

    public void delete(String alias) {
        Certificate parent = certificateService.findByAlias(alias);
        List<Certificate> children = certificateService.traverseSubtree(parent);

        Collections.reverse(children);

        for (Certificate child : children) {
            keyStoreDeleter.deleteCertificate(certStore, certStorePass, alias);

            String filename = String.format("%s/%s.pem", privateKeyStore, alias);
            pemKeyStore.delete(filename);

            certificateService.remove(child);
        }
    }


}
