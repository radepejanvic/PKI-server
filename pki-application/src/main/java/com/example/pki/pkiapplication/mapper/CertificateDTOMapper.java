package com.example.pki.pkiapplication.mapper;

import com.example.pki.pkiapplication.dto.CertificateDTO;
import com.example.pki.pkiapplication.model.Certificate;

import javax.security.auth.x500.X500Principal;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class CertificateDTOMapper {

    public static Certificate fromDTO(CertificateDTO dto) {
        Certificate cert = new Certificate();

        return null;
    }

    public static CertificateDTO toDTO(Certificate cert) {
        CertificateDTO dto = new CertificateDTO();

        dto.setId(cert.getId());
        dto.setSerialNumber(cert.getSerialNumber());
        dto.setIssuedOn(cert.getIssuedOn());
        dto.setExpiresOn(cert.getExpiresOn());
        dto.setType(cert.getType());
        dto.setCertificateAlias(cert.getAlias());

        if (cert.getIssuer() != null) {
            dto.setIssuerId(cert.getIssuer().getId());
            dto.setIssuerAlias(cert.getIssuer().getAlias());
        }

        return dto;
    }


    public static CertificateDTO toDTO(Certificate cert, X509Certificate x509) {
        CertificateDTO dto = new CertificateDTO();

        dto.setId(cert.getId());
        dto.setSerialNumber(cert.getSerialNumber());
        dto.setCertificateAlias(cert.getAlias());


        if(cert.getIssuer() != null) {
            dto.setIssuerId(cert.getIssuer().getId());
            dto.setIssuerAlias(cert.getIssuer().getAlias());
        }

        HashMap<String, String> subject = parseString(x509.getSubjectX500Principal().toString());
        HashMap<String, String> issuer = parseString(x509.getIssuerX500Principal().toString());

        dto.setSubjectCN(subject.get("CN"));
        dto.setSubjectO(subject.get("O"));
        dto.setSubjectC(subject.get("C"));
        dto.setSubjectE(subject.get("EMAILADDRESS"));

        dto.setIssuerCN(issuer.get("CN"));
        dto.setIssuerO(issuer.get("O"));
        dto.setIssuerC(issuer.get("C"));
        dto.setIssuerE(issuer.get("EMAILADDRESS"));

        dto.setIssuedOn(x509.getNotBefore().getTime());
        dto.setExpiresOn(x509.getNotAfter().getTime());

        dto.setType(cert.getType());

        dto.setSignature(bytesToHex(x509.getSignature()));
        dto.setPublicKey(bytesToHex(x509.getPublicKey().getEncoded()));

        return dto;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static HashMap<String, String> parseString(String input) {
        HashMap<String, String> keyValuePairs = new HashMap<>();

        // Split the input string by comma
        String[] parts = input.split(", ");

        for (String part : parts) {
            // Further split each part to extract key-value pair
            String[] keyValue = part.split("=");

            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                keyValuePairs.put(key, value);
            }
        }

        return keyValuePairs;
    }

}
