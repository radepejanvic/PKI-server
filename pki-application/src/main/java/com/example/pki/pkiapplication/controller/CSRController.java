package com.example.pki.pkiapplication.controller;

import com.example.pki.pkiapplication.dto.CSRDTO;
import com.example.pki.pkiapplication.model.CSR;
import com.example.pki.pkiapplication.model.enums.CSRStatus;
import com.example.pki.pkiapplication.service.CSRService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.pki.pkiapplication.mapper.CSRDTOMapper.fromDTO;
import static com.example.pki.pkiapplication.mapper.CSRDTOMapper.fromModel;

@Controller
@Validated
@RequestMapping("/api/csr")
public class CSRController {

    @Autowired
    private CSRService csrService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCSR(@Valid @RequestBody CSRDTO csrdto) throws Exception {

        CSR csr = fromDTO(csrdto);
        csr.setStatus(CSRStatus.PENDING);
        csrService.save(csr);

        return new ResponseEntity<CSRDTO>(fromModel(csr), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPending(){

        List<CSRDTO> csrdtos = csrService.findAll().stream()
                .filter(csr -> csr.getStatus() == CSRStatus.PENDING)
                .map(csr -> fromModel(csr))
                .collect(Collectors.toList());

        return new ResponseEntity<Collection<CSRDTO>>(csrdtos, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOne(@PathVariable("id") Long id){
        CSR csr = csrService.findOne(id);

        if(csr == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<CSRDTO>(fromModel(csr), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> rejectCSR(@PathVariable("id") Long id){
        CSR csr = csrService.findOne(id);

        if(csr == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        csr.setStatus(CSRStatus.REJECTED);
        csrService.save(csr);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
