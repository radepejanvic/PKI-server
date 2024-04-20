package com.example.pki.pkiapplication.service.impl;

import com.example.pki.pkiapplication.service.KeyStoringService;

import java.io.*;
import java.security.PrivateKey;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PemKeyStoringService implements KeyStoringService {

    @Value(value="${keys}")
    private String directory;

    @Override
    public void write(PrivateKey key, String alias) {
        String filename = String.format("%s/%s.pem", directory, alias);

        try (Writer writer = new FileWriter(filename);
             JcaPEMWriter pemWriter = new JcaPEMWriter(writer)) {
            pemWriter.writeObject(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PrivateKey read(String alias) {
        String filename = String.format("%s/%s.pem", directory, alias);

        try (FileReader keyReader = new FileReader(filename)) {

            PEMParser pemParser = new PEMParser(keyReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(pemParser.readObject());

            return converter.getPrivateKey(privateKeyInfo);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
