package com.gestionbudget.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// import javax.annotation.processing.Generated;

@Table(name ="client")
@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    public void setPassword(String password) {
        this.password = password;
    }
}
