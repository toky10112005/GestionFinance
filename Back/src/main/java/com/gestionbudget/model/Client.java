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

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;

    @Column (name = "updated_at", nullable = false)
    private LocalDateTime updated_at;
}
