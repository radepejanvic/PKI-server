package com.example.pki.pkiapplication.mapper;

import com.example.pki.pkiapplication.dto.CertificateDTO;
import com.example.pki.pkiapplication.model.Certificate;

public class CertificateDTOMapper {

    public static Certificate fromDTO(CertificateDTO dto) {
        Certificate cert = new Certificate();

        return null;
    }

    public static CertificateDTO toDTO(Certificate cert) {
        CertificateDTO dto = new CertificateDTO();

        dto.setId(cert.getId());
        dto.setSerialNumber(cert.getSerialNumber());
        dto.setSubject(cert.getSubject());
        dto.setIssuedOn(cert.getIssuedOn());
        dto.setExpiresOn(cert.getExpiresOn());
//        dto.setExtensions(cert.getExtensions());
        dto.setType(cert.getType());

        return dto;
    }

}
