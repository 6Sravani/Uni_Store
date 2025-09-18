-- =================================================================================
-- Table: product_categories
-- Purpose: Stores product categories in a hierarchical structure.
-- =================================================================================
CREATE TABLE product_categories (
    -- Primary key for the category
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    -- Foreign key to enable nested categories (parent-child relationship).
    -- Can be NULL for top-level categories.
    `parent_category_id` BIGINT UNSIGNED,

    -- The display name of the category.
    `name` VARCHAR(100) NOT NULL,

    -- URL-friendly version of the name, must be unique for clean URLs.
    `slug` VARCHAR(255) NOT NULL UNIQUE,

    -- Optional longer description for the category page.
    `description` TEXT,

    -- Audit timestamps.
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),

    -- Defines the self-referencing foreign key relationship.
    -- The default behavior (ON DELETE RESTRICT) will prevent deleting a parent
    -- category if it still has children, which is a safe default.
    CONSTRAINT `fk_categories_parent`
                FOREIGN KEY (`parent_category_id`)
                            REFERENCES `product_categories` (`id`)
);








-- =================================================================================
-- Table: products
-- Purpose: Stores the base information for a product concept.
-- =================================================================================
CREATE TABLE products (
    -- Primary key for the base product.
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    -- The main display name of the product.
    `name` VARCHAR(255) NOT NULL,

    -- A long-form description for the product detail page.
    `description` TEXT,

    -- A URL-friendly version of the name, must be unique.
    `slug` VARCHAR(255) NOT NULL UNIQUE,

    -- A flag to show/hide the product from the storefront.
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit timestamps that are managed automatically by the database.
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`)
);








-- =================================================================================
-- Table: product_category_assignments
-- Purpose: A linking table to create the many-to-many relationship
--          between products and categories.
-- =================================================================================
CREATE TABLE product_category_assignments (
    -- Foreign key pointing to the products table.
    `product_id` BIGINT UNSIGNED NOT NULL,

    -- Foreign key pointing to the product_categories table.
    `category_id` BIGINT UNSIGNED NOT NULL,

    -- A composite primary key ensures that a product can only be assigned
    -- to a specific category once, preventing duplicate entries.
    PRIMARY KEY (`product_id`, `category_id`),

    -- Constraint for the product_id foreign key.
    -- If a product is deleted, its assignments are automatically removed.
    CONSTRAINT `fk_assignments_products`
            FOREIGN KEY (`product_id`)
                    REFERENCES `products` (`id`)
                            ON DELETE CASCADE,

    -- Constraint for the category_id foreign key.
    -- If a category is deleted, its assignments are automatically removed.
    CONSTRAINT `fk_assignments_categories`
            FOREIGN KEY (`category_id`)
                    REFERENCES `product_categories` (`id`)
                            ON DELETE CASCADE
);







-- =================================================================================
-- Table: product_variants
-- Purpose: Stores the specific, sellable variations of a base product.
-- =================================================================================
CREATE TABLE product_variants (
    -- Primary key for the specific product variant.
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    -- Foreign key linking back to the parent product.
    `product_id` BIGINT UNSIGNED NOT NULL,

    -- Stock Keeping Unit: A unique code for inventory management.
    `sku` VARCHAR(100) NOT NULL UNIQUE,

    -- The price for this specific variant, DECIMAL for financial accuracy.
    `price` DECIMAL(10, 2) NOT NULL,

    -- The cost of the item for the business (for profit calculation).
    `cost` DECIMAL(10, 2),

    -- The current stock level for this specific variant.
    `quantity` INT UNSIGNED NOT NULL DEFAULT 0,

    -- An optional, specific image for this variant (e.g., the red shirt).
    `image_url` VARCHAR(512),

    -- A flag to show/hide this specific variant.
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit timestamps that are managed automatically by the database.
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),

    -- Defines the foreign key relationship to the parent product.
    -- If a base product is deleted, all its variants are also deleted.
    CONSTRAINT `fk_variants_products`
            FOREIGN KEY (`product_id`)
                    REFERENCES `products` (`id`)
                            ON DELETE CASCADE
);








-- =================================================================================
-- Table: product_attributes
-- Purpose: Stores the types of product options (e.g., 'Size', 'Color').
-- =================================================================================
CREATE TABLE product_attributes (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`)
);







-- =================================================================================
-- Table: product_attribute_values
-- Purpose: Stores the specific values for each attribute (e.g., 'S', 'M', 'Red').
-- =================================================================================
CREATE TABLE product_attribute_values (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `product_attribute_id` BIGINT UNSIGNED NOT NULL,
    `value` VARCHAR(100) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`id`),

    -- Ensures that a value ("Red") can only exist once for a given attribute ("Color").
    UNIQUE KEY `uq_attribute_value` (`product_attribute_id`, `value`),

    -- If an attribute type like "Color" is deleted, all its values ("Red", "Blue") are also deleted.
    CONSTRAINT `fk_attribute_values_to_attributes`
            FOREIGN KEY (`product_attribute_id`)
                    REFERENCES `product_attributes` (`id`)
                            ON DELETE CASCADE
);






-- =================================================================================
-- Table: product_variant_attributes
-- Purpose: A linking table to connect a variant to its defining attributes.
-- =================================================================================
CREATE TABLE product_variant_attributes (
    -- Foreign key pointing to the specific, sellable product variant.
    `product_variant_id` BIGINT UNSIGNED NOT NULL,

    -- Foreign key pointing to a specific attribute value (e.g., 'Medium', 'Red').
    `product_attribute_value_id` BIGINT UNSIGNED NOT NULL,

    -- A composite primary key ensures that a variant can only have a specific
    -- attribute value assigned once (e.g., can't be both 'Small' and 'Medium').
    PRIMARY KEY (`product_variant_id`, `product_attribute_value_id`),

    -- Constraint for the product_variant_id foreign key.
    -- If a variant is deleted, its attribute links are automatically removed.
    CONSTRAINT `fk_variant_attributes_to_variants`
            FOREIGN KEY (`product_variant_id`)
                    REFERENCES `product_variants` (`id`)
                            ON DELETE CASCADE,

    -- Constraint for the product_attribute_value_id foreign key.
    -- If an attribute value (e.g., 'Small') is deleted, this link is also removed.
    CONSTRAINT `fk_variant_attributes_to_attribute_values`
            FOREIGN KEY (`product_attribute_value_id`)
                    REFERENCES `product_attribute_values` (`id`)
                            ON DELETE CASCADE
);




-- =================================================================================
-- Table: product_images
-- Purpose: Stores multiple images for products and their specific variants.
-- =================================================================================
CREATE TABLE product_images (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    -- An image can belong to a base product (e.g., a lifestyle shot).
    `product_id` BIGINT UNSIGNED,

    -- An image can also belong to a specific variant (e.g., the red color shirt).
    `product_variant_id` BIGINT UNSIGNED,

    -- The URL of the image, hosted on a CDN or file server.
    `image_url` VARCHAR(512) NOT NULL,

    -- Descriptive text for accessibility (screen readers) and SEO.
    `alt_text` VARCHAR(255),

    -- A number to control the display order (e.g., 0 is the main image).
    `sort_order` INT NOT NULL DEFAULT 0,

    PRIMARY KEY (`id`),

    -- Foreign key to the base product table.
    CONSTRAINT `fk_images_to_products`
            FOREIGN KEY (`product_id`)
                    REFERENCES `products` (`id`)
                            ON DELETE CASCADE,

    -- Foreign key to the specific product variant table.
    CONSTRAINT `fk_images_to_variants`
            FOREIGN KEY (`product_variant_id`)
                    REFERENCES `product_variants` (`id`)
                            ON DELETE CASCADE,

    -- Ensures that an image is linked to EITHER a product OR a variant, but not either.
    CONSTRAINT `chk_image_link` CHECK (`product_id` IS NOT NULL OR `product_variant_id` IS NOT NULL)
);


