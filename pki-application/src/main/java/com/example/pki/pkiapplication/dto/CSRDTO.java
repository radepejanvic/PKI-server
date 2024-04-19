package com.example.pki.pkiapplication.dto;

import com.example.pki.pkiapplication.model.CSRStatus;
import com.example.pki.pkiapplication.model.CertificateType;
import jakarta.validation.constraints.Email;
import lombok.Data;

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
}
