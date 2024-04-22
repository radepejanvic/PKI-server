package com.example.pki.pkiapplication.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

@Component
public class KeyStoreDeleter {

    public void deleteCertificate(String keyStoreFile, String keyStorePass, String alias)  {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS", "SUN");

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
            keyStore.load(in, keyStorePass.toCharArray());

            if (keyStore.containsAlias(alias)) {
                keyStore.deleteEntry(alias);

                try (FileOutputStream fos = new FileOutputStream(keyStoreFile)) {
                    keyStore.store(fos, keyStorePass.toCharArray());
                    System.out.println("Entry with alias '" + alias + "' removed successfully.");
                }
            } else {
                System.out.println("Entry with alias '" + alias + "' not found in the keystore.");
            }
        } catch (FileNotFoundException | KeyStoreException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
