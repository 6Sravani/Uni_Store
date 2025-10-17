-- =================================================================================
-- Table: carts
-- Purpose: Stores a persistent shopping cart for each user, using UUIDs.
-- =================================================================================
CREATE TABLE carts (
    -- Primary key is a UUID, stored efficiently as a 16-byte binary.
    -- The database will automatically generate a new UUID on insert.
                       `id` BINARY(16) NOT NULL DEFAULT (UUID_TO_BIN(UUID())) PRIMARY KEY,

    -- Foreign key to the user who owns this cart.
    -- The UNIQUE constraint ensures a user can only have one cart.
                       `user_id` BIGINT UNSIGNED NOT NULL UNIQUE,

    -- Audit timestamps.
                       `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- If a user is deleted, their cart is also automatically deleted.
                       CONSTRAINT `fk_carts_user`
                           FOREIGN KEY (`user_id`)
                               REFERENCES `users` (`id`)
                               ON DELETE CASCADE
);

-- =================================================================================
-- Table: cart_items
-- Purpose: Stores the individual product variants within a user's cart.
-- =================================================================================
CREATE TABLE cart_items (
    -- Primary key for the cart item.
                            `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,

    -- Foreign key linking back to the parent cart's UUID.
    -- The data type MUST match the carts.id column.
                            `cart_id` BINARY(16) NOT NULL,

    -- Foreign key linking to the specific product variant added to the cart.
                            `product_variant_id` BIGINT UNSIGNED NOT NULL,

    -- The quantity of this specific item in the cart.
                            `quantity` INT UNSIGNED NOT NULL,

    -- Audit timestamps.
                            `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Prevents adding the same product variant to a cart more than once.
    -- To change quantity, the existing row should be updated.
                            UNIQUE KEY `uq_cart_item` (`cart_id`, `product_variant_id`),

    -- If a cart is deleted, all its items are automatically deleted.
                            CONSTRAINT `fk_cart_items_cart`
                                FOREIGN KEY (`cart_id`)
                                    REFERENCES `carts` (`id`)
                                    ON DELETE CASCADE,

    -- If a product variant is deleted from the store, it's also removed from all carts.
                            CONSTRAINT `fk_cart_items_product_variant`
                                FOREIGN KEY (`product_variant_id`)
                                    REFERENCES `product_variants` (`id`)
                                    ON DELETE CASCADE
);