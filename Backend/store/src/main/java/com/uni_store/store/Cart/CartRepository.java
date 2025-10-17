package com.uni_store.store.Cart;

import com.uni_store.store.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

  Optional<Cart> findByUser(User user);
}