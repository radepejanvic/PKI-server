package com.example.pki.pkiapplication.controller;

import com.example.pki.pkiapplication.dto.CSRDTO;
import com.example.pki.pkiapplication.dto.CertificateDTO;
import com.example.pki.pkiapplication.mapper.CSRDTOMapper;
import com.example.pki.pkiapplication.mapper.CertificateDTOMapper;
import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.service.CertificateService;
import com.example.pki.pkiapplication.service.impl.CertificateGeneratingServiceImpl;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateGeneratingServiceImpl certificateGeneratingService;

    @GetMapping
    public ResponseEntity<?> getAll() {

        List<CertificateDTO> certificates = certificateService.findAll().stream()
                .map(CertificateDTOMapper::toDTO)
                .toList();

        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        Certificate certificate = certificateService.findOne(id);

        if (certificate == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

        return new ResponseEntity<>(CertificateDTOMapper.toDTO(certificate), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> generateCertificate(@RequestBody CSRDTO dto) {

        CSR csr = CSRDTOMapper.fromDTO(dto);

        Certificate cert = null;

        if (csr.getSubjectAlias().equals(csr.getIssuerAlias())) {
            cert = certificateGeneratingService.generateRoot(csr);
        } else {
            cert = certificateGeneratingService.generateEE(csr);

            Certificate issuerCert = certificateService.findByAlias(csr.getIssuerAlias());

            if (issuerCert == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            cert.setIssuer(issuerCert);
        }

        cert.setAlias(csr.getSubjectAlias());
        certificateService.save(cert);

//        certificateGeneratingService.generateCA(csr);
//        certificateGeneratingService.generateRoot(csr);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
