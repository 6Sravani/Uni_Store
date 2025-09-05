package com.uni_store.store.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter
@ToString(exclude = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false,unique = true)
    private String email;

    @NotBlank
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", nullable = false, length = 60)
    private String passwordHash;

    @NotBlank
    @Size(min=2,max=100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(min = 2, max = 100)
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private UserRole role=UserRole.CUSTOMER;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @NotNull
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Address> addresses = new LinkedHashSet<>();

    // Utility methods for maintaining relationship integrity
    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }

    public void changeEmail(String email) {
        this.email = email;
        this.emailVerified = false;
    }
    public void changePassword(String password) {
        this.passwordHash = password;
    }
    public void changeName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public boolean isAdmin(){
        return UserRole.ADMIN.equals(this.role);
    }
    public void promoteToAdmin() {
        this.role = UserRole.ADMIN;
    }
    public void demoteToCustomer() {
        this.role = UserRole.CUSTOMER;
    }
    public boolean isActive() {
        return isActive;
    }
    public void activate() {
        this.isActive = true;
    }
    public void deactivate() {
        this.isActive = false;
    }
    public boolean isEmailVerified() {
        return emailVerified;
    }
    public void verifyEmail() {
        this.emailVerified = true;
    }
    public void unVerifyEmail() {
        this.emailVerified = false;
    }
    public boolean canLogin() {
        return isActive && emailVerified;
    }
    public Optional<Address> getDefaultAddress(AddressType addressType) {
        return addresses.stream()
                .filter(address -> addressType.equals(address.getAddressType()) && address.getIsDefault())
                .findFirst();
    }
}