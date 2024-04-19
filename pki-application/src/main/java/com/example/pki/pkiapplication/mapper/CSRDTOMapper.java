package com.example.pki.pkiapplication.mapper;

import com.example.pki.pkiapplication.dto.CSRDTO;
import com.example.pki.pkiapplication.model.CSR;

public class CSRDTOMapper {

    public static CSR fromDTO(CSRDTO csrdto){
        CSR csr = new CSR();
        csr.setEmail(csrdto.getEmail());
        csr.setId(csrdto.getId());
        csr.setCountry(csrdto.getCountry());
        csr.setCommonName(csrdto.getCommonName());
        csr.setOrganization(csrdto.getOrganization());
        csr.setTemplate(csrdto.getTemplate());
        csr.setStatus(csrdto.getStatus());
        return csr;
    }

    public static CSRDTO fromModel(CSR csr){
        CSRDTO csrdto = new CSRDTO();
        csrdto.setEmail(csr.getEmail());
        csrdto.setId(csr.getId());
        csrdto.setCountry(csr.getCountry());
        csrdto.setCommonName(csr.getCommonName());
        csrdto.setOrganization(csr.getOrganization());
        csrdto.setTemplate(csr.getTemplate());
        csrdto.setStatus(csr.getStatus());
        return csrdto;
    }
}
