package com.uni_store.store.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface addressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
