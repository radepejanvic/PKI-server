package com.example.pki.pkiapplication.model;

import com.example.pki.pkiapplication.dto.UserCredentialsDTO;
import com.example.pki.pkiapplication.model.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users-pki")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Transient
    private String jwt;


    public User(UserCredentialsDTO userCredentialsDTO){
        this.id = userCredentialsDTO.getId();
        this.email = userCredentialsDTO.getEmail();
        this.password = userCredentialsDTO.getPassword();
        this.userType = userCredentialsDTO.getUserType();
    }
}
