package com.computerstore.backend.service;

import com.computerstore.backend.dto.AddressRequest;
import com.computerstore.backend.dto.AddressResponse;
import com.computerstore.backend.entity.Address;
import com.computerstore.backend.entity.User;
import com.computerstore.backend.exception.ResourceNotFoundException;
import com.computerstore.backend.repository.AddressRepository;
import com.computerstore.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Créer une nouvelle adresse
     */
    public AddressResponse createAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Si c'est la première adresse ou si isDefault est true
        if (request.getIsDefault()) {
            // Désactiver les autres adresses par défaut
            addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        Address address = new Address();
        address.setUser(user);
        address.setFirstName(request.getFirstName());
        address.setLastName(request.getLastName());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setCountry(request.getCountry());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setIsDefault(request.getIsDefault());
        address.setAddressType(request.getAddressType());
        address.setInstructions(request.getInstructions());

        Address saved = addressRepository.save(address);
        return convertToResponse(saved);
    }

    /**
     * Récupérer toutes les adresses d'un utilisateur
     */
    public List<AddressResponse> getAddressesByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer une adresse spécifique
     */
    public AddressResponse getAddressById(Long userId, Long addressId) {
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        return convertToResponse(address);
    }

    /**
     * Mettre à jour une adresse
     */
    public AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request) {
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Si isDefault change
        if (request.getIsDefault() && !address.getIsDefault()) {
            addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        address.setFirstName(request.getFirstName());
        address.setLastName(request.getLastName());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setCountry(request.getCountry());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setIsDefault(request.getIsDefault());
        address.setAddressType(request.getAddressType());
        address.setInstructions(request.getInstructions());

        Address updated = addressRepository.save(address);
        return convertToResponse(updated);
    }

    /**
     * Supprimer une adresse
     */
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Si c'était l'adresse par défaut, en désigner une autre
        if (address.getIsDefault()) {
            List<Address> otherAddresses = addressRepository.findByUserId(userId).stream()
                    .filter(a -> !a.getId().equals(addressId))
                    .collect(Collectors.toList());

            if (!otherAddresses.isEmpty()) {
                otherAddresses.get(0).setIsDefault(true);
                addressRepository.save(otherAddresses.get(0));
            }
        }

        addressRepository.deleteById(addressId);
    }

    /**
     * Obtenir l'adresse par défaut d'un utilisateur
     */
    public AddressResponse getDefaultAddress(Long userId) {
        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No default address found"));

        return convertToResponse(address);
    }

    /**
     * Définir une adresse comme adresse par défaut
     */
    public AddressResponse setDefaultAddress(Long userId, Long addressId) {
        Address newDefault = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Désactiver l'ancienne adresse par défaut
        addressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(addr -> {
            addr.setIsDefault(false);
            addressRepository.save(addr);
        });

        newDefault.setIsDefault(true);
        Address updated = addressRepository.save(newDefault);
        return convertToResponse(updated);
    }

    /**
     * Obtenir les adresses d'un type spécifique
     */
    public List<AddressResponse> getAddressesByType(Long userId, String addressType) {
        return addressRepository.findByUserIdAndAddressType(userId, addressType).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convertir entité en DTO
     */
    private AddressResponse convertToResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setFirstName(address.getFirstName());
        response.setLastName(address.getLastName());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setZipCode(address.getZipCode());
        response.setCountry(address.getCountry());
        response.setPhoneNumber(address.getPhoneNumber());
        response.setIsDefault(address.getIsDefault());
        response.setAddressType(address.getAddressType());
        response.setInstructions(address.getInstructions());
        response.setCreatedAt(address.getCreatedAt());
        response.setUpdatedAt(address.getUpdatedAt());
        return response;
    }
}
