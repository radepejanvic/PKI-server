package com.example.pki.pkiapplication.controller;

import com.example.pki.pkiapplication.dto.CSRDTO;
import com.example.pki.pkiapplication.dto.CertificateDTO;
import com.example.pki.pkiapplication.mapper.CSRDTOMapper;
import com.example.pki.pkiapplication.mapper.CertificateDTOMapper;
import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.Certificate;
import com.example.pki.pkiapplication.model.enums.CSRStatus;
import com.example.pki.pkiapplication.model.enums.CertificateType;
import com.example.pki.pkiapplication.service.CSRService;
import com.example.pki.pkiapplication.service.CertificateService;
import com.example.pki.pkiapplication.service.impl.CertificateGeneratingServiceImpl;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.pki.pkiapplication.mapper.CertificateDTOMapper.toDTO;

@RestController
@Validated
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CSRService csrService;


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

        return new ResponseEntity<>(toDTO(certificate), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> generateCertificate(@RequestBody CSRDTO dto) {

        CSR csr = CSRDTOMapper.fromDTO(dto);
        Certificate cert = certificateGeneratingService.generateCertificate(csr);

        if (cert == null) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }

        if (csr.getTemplate() != CertificateType.SS) {
            Certificate issuerCert = certificateService.findByAlias(csr.getIssuerAlias());

            // TODO: Add function for checking the issuers permission for issuing certificates.
            if (issuerCert == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

            cert.setIssuer(issuerCert);
        }

        cert.setAlias(csr.getSubjectAlias());
        cert.setType(csr.getTemplate());
        certificateService.save(cert);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping(value = "/{id}")
    public ResponseEntity<?> acceptCSR(@PathVariable("id") Long id){

        CSR csr = csrService.findOne(id);
        if(csr == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Certificate cert = certificateGeneratingService.generateCertificate(csr);
        if (cert == null) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }

        if (csr.getTemplate() != CertificateType.SS) {
            Certificate issuerCert = certificateService.findByAlias(csr.getIssuerAlias());

            // TODO: Add function for checking the issuers permission for issuing certificates.
            if (issuerCert == null) { return new ResponseEntity<>(HttpStatus.NOT_FOUND); }

            cert.setIssuer(issuerCert);
        }

        cert.setAlias(csr.getSubjectAlias());
        cert.setType(csr.getTemplate());
        certificateService.save(cert);

        csr.setStatus(CSRStatus.APPROVED);
        csrService.save(csr);

        return new ResponseEntity<>(toDTO(cert), HttpStatus.CREATED);
    }


    @GetMapping(value="/{subjectAlias}" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> hasCert(@PathVariable String subjectAlias){
        Certificate cert = certificateService.findByAlias(subjectAlias);

        if (cert == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cert, HttpStatus.OK);
    }
}
