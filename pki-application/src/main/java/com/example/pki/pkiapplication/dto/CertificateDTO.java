package com.example.pki.pkiapplication.dto;

import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.model.CertificateType;
import com.example.pki.pkiapplication.model.Extension;
import lombok.Data;

import java.math.BigInteger;
import java.util.HashSet;

@Data
public class CertificateDTO {

    private Long id;

    private BigInteger serialNumber;

    private String subject;

    private Long issuedOn;

    private Long expiresOn;

    private HashSet<Extension> extensions;

    private Certificate issuer;

    private CertificateType type;
}
