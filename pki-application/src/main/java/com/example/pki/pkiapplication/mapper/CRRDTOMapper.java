package com.example.pki.pkiapplication.mapper;

import com.example.pki.pkiapplication.dto.CRRDTO;
import com.example.pki.pkiapplication.model.CRR;
import com.example.pki.pkiapplication.model.Certificate;

public class CRRDTOMapper {

    public static CRR fromDTO(CRRDTO dto, Certificate cert) {
        CRR crr = new CRR();

        crr.setCertificate(cert);
        crr.setReason(dto.getReason());

        return crr;
    }

    public static CRRDTO toDTO(CRR crr) {
        CRRDTO dto = new CRRDTO();

        dto.setId(crr.getId());
        dto.setSerialNumber(crr.getCertificate().getSerialNumber());
        dto.setReason(crr.getReason());

        return dto;
    }

}
