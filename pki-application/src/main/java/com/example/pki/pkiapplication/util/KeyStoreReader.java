package com.example.pki.pkiapplication.util;

import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Component
public class KeyStoreReader {

    private KeyStore keyStore;

    public KeyStoreReader() {
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, keyStorePass.toCharArray());

            if(keyStore.isKeyEntry(alias)) {
                Certificate cert = keyStore.getCertificate(alias);
                return cert;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, keyStorePass.toCharArray());

            if(keyStore.isKeyEntry(alias)) {
                PrivateKey pk = (PrivateKey) keyStore.getKey(alias, pass.toCharArray());
                return pk;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }



}
