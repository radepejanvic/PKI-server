package com.example.pki.pkiapplication.dto;

import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.model.enums.CertificateType;
import com.example.pki.pkiapplication.model.Extension;
import lombok.Data;

import java.math.BigInteger;
import java.util.HashSet;

@Data
public class CertificateDTO {

    private Long id;

    private BigInteger serialNumber;

    // TODO: Remove
    private String certificateAlias;


    private String subjectCN;

    private String subjectO;

    private String subjectC;

    private String subjectE;


    private Long issuerId;

    // TODO: Remove
    private String issuerAlias;


    private String issuerCN;

    private String issuerO;

    private String issuerC;

    private String issuerE;


    private Long issuedOn;

    private Long expiresOn;

    private CertificateType type;

    private String signature;

    private String publicKey;

}
