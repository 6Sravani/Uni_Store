-- =============================================
-- User Domain Tables
-- Core identity and authentication tables for the university e-commerce platform
-- =============================================

-- Users table: Stores core user identity and authentication information
CREATE TABLE users (
    -- Primary key: Unique identifier for each user
                       id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,

    -- Login identifier: Must be unique across all users
                       email VARCHAR(255) NOT NULL UNIQUE,

    -- Password storage: BCrypt hashed password (always exactly 60 characters)
                       password_hash VARCHAR(60) NOT NULL,

    -- Personal identification details
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,

    -- Authorization role: Uses application enum values ('CUSTOMER', 'ADMIN')
                       role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',

    -- Account status flags
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,      -- Soft delete flag
                       email_verified BOOLEAN NOT NULL DEFAULT FALSE, -- Email confirmation status

    -- Audit timestamps
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,          -- Record creation time
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- Last update time
);

-- Addresses table: Stores user addresses for shipping and billing
CREATE TABLE addresses (
    -- Primary key: Unique identifier for each address
                           id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,

    -- Foreign key: References the user who owns this address
                           user_id BIGINT UNSIGNED NOT NULL,

    -- Address type: Restricted to either SHIPPING or BILLING
                           address_type ENUM('SHIPPING', 'BILLING') NOT NULL,

    -- Address components
                           street_line1 VARCHAR(255) NOT NULL,    -- Primary street address
                           street_line2 VARCHAR(255),             -- Optional secondary address line
                           city VARCHAR(100) NOT NULL,
                           state VARCHAR(100) NOT NULL,
                           country VARCHAR(100) NOT NULL,
                           zip_code VARCHAR(20) NOT NULL,         -- Postal code

    -- Address preference flag
                           is_default BOOLEAN NOT NULL DEFAULT FALSE, -- Marks preferred address for its type

    -- Audit timestamps
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,          -- Record creation time
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Last update time

    -- Foreign key constraint: Ensures address belongs to a valid user
    -- CASCADE deletion: If a user is deleted, all their addresses are also deleted
                           CONSTRAINT address_user_id_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =============================================
-- Notes on Design Decisions:
-- 1. UNSIGNED BIGINT: Provides larger range of positive values for IDs
-- 2. VARCHAR(60) for password_hash: Precisely sized for BCrypt hashes
-- 3. ENUM for address_type: Restricts to valid values only
-- 4. ON DELETE CASCADE: Maintains referential integrity automatically
-- 5. DEFAULT values: Ensure sensible defaults for common cases
-- 6. TIMESTAMP defaults: Automatically track creation and update times
-- =============================================