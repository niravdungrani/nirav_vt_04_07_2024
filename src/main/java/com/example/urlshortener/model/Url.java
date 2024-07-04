package com.example.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destinationUrl;

    @Column(unique = true)
    private String shortUrl;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}