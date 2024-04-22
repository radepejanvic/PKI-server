package com.example.pki.pkiapplication.dto;

import com.example.pki.pkiapplication.model.enums.CSRStatus;
import com.example.pki.pkiapplication.model.enums.CertificateType;
import com.example.pki.pkiapplication.model.enums.KeyUsage;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.util.List;

@Data
public class CSRDTO {
    private Long id;
    private String publicKey;
    private String commonName;
    private String organization;
    private String country;
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;
    private CertificateType template;
    private CSRStatus status;
    private String issuerAlias;
    private String subjectAlias;
    private List<KeyUsage> keyUsages;
    private String domainName;
}
