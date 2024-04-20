package com.example.pki.pkiapplication.utils;

import org.springframework.stereotype.Component;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;

@Component
public class RSAKeyGenerator implements KeyGenerator{

    public RSAKeyGenerator() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    public KeyPair generateKeys() {
        try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);

            return keyGen.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

}
