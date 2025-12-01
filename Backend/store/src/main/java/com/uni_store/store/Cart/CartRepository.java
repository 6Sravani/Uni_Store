package com.uni_store.store.Cart;

import com.uni_store.store.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

  Optional<Cart> findByUser(User user);

  @Query("SELECT c FROM Cart c " +
          "LEFT JOIN FETCH c.cartItems ci " +
          "LEFT JOIN FETCH ci.productVariant pv " +
          "LEFT JOIN FETCH pv.product " +
          "WHERE c.user = :user")
  Optional<Cart> findByUserWithDetails(@Param("user") User user);
}