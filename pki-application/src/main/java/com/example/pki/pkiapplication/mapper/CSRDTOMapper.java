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
        csr.setIssuerAlias(csrdto.getIssuerAlias());
        csr.setPublicKey(csrdto.getPublicKey());
        csr.setSubjectAlias(csrdto.getSubjectAlias());
        csr.setKeyUsages(csrdto.getKeyUsages());
        csr.setDomainName(csrdto.getDomainName());
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
        csrdto.setIssuerAlias(csr.getIssuerAlias());
        csrdto.setPublicKey(csr.getPublicKey());
        csrdto.setSubjectAlias(csr.getSubjectAlias());
        csrdto.setKeyUsages(csr.getKeyUsages());
        csrdto.setDomainName(csr.getDomainName());
        return csrdto;
    }
}
