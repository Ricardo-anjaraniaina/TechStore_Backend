package com.computerstore.backend.repository;

import com.computerstore.backend.entity.Address;
import com.computerstore.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    List<Address> findByUserId(Long userId);
    Optional<Address> findByIdAndUserId(Long id, Long userId);
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
    List<Address> findByUserIdAndAddressType(Long userId, String addressType);
    void deleteByIdAndUserId(Long id, Long userId);
}
