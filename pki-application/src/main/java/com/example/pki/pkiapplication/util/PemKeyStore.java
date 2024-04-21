package com.example.pki.pkiapplication.util;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.PrivateKey;

@Component
public class PemKeyStore {

    public void write(String filename, PrivateKey key) {
        try (Writer writer = new FileWriter(filename);
             JcaPEMWriter pemWriter = new JcaPEMWriter(writer)) {
            pemWriter.writeObject(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrivateKey read(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            PEMParser pemParser = new PEMParser(reader);
            Object object = pemParser.readObject();
            if (object instanceof PEMKeyPair) {
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                return converter.getPrivateKey(((PEMKeyPair) object).getPrivateKeyInfo());
            } else {
                throw new IllegalArgumentException("Expecting a PEMKeyPair object in the PEM file.");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
