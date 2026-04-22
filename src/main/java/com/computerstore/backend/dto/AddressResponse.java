package com.computerstore.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phoneNumber;
    private Boolean isDefault;
    private String addressType;
    private String instructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Méthode utilitaire pour afficher l'adresse complète
    public String getFullAddress() {
        return String.format("%s, %s %s, %s, %s", street, city, zipCode, state, country);
    }
}
