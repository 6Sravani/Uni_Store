package com.uni_store.store.User;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class USerService {
    private final UserRepository userRepository;
    private User createUser(String email,String password, String firstName, String lastName) {
        if(userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .email(email)
                .passwordHash(password)
                .firstName(firstName)
                .lastName(lastName)
                .build();
        return userRepository.save(user);
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found"));
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found"));
    }
    public User updateUser(Long id, String email, String password, String firstName, String lastName) {
        User user = getUserByIdOrThrow(id);
        if(email != null && !email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        if(firstName != null || lastName != null) {
            user.changeName(
                    firstName != null ? firstName:user.getFirstName(),
                    lastName != null ? lastName:user.getLastName()
            );
        }
        if(email != null && !email.equals(user.getEmail())){
            user.changeEmail(email);
        }
        if(password != null && !password.equals(user.getPasswordHash())) {
            user.changePassword(password);
        }
        return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public User deactivateUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.deactivate();
        return userRepository.save(user);
    }

    public User activateUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.activate();
        return userRepository.save(user);
    }

    public User markEmailAsVerified(Long id) {
        User user = getUserByIdOrThrow(id);
        user.verifyEmail();
        return userRepository.save(user);
    }

    public User markEmailAsUnverified(Long id) {
        User user = getUserByIdOrThrow(id);
        user.unVerifyEmail();
        return userRepository.save(user);
    }
    public User changePassword(Long id, String password) {
        User user = getUserByIdOrThrow(id);
        user.changePassword(password);
        return userRepository.save(user);
    }

    public User promoteToAdmin(Long id) {
        User user = getUserByIdOrThrow(id);
        user.promoteToAdmin();
        return userRepository.save(user);
    }

    public User demoteToCustomer(Long id) {
        User user = getUserByIdOrThrow(id);
        user.demoteToCustomer();
        return userRepository.save(user);
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public List<User> getUsersByEmailVerifiedStatus(Boolean emailVerified) {
        return userRepository.findByEmailVerified(emailVerified);
    }

    public long countActiveUsers() {
        return userRepository.findByIsActiveTrue().size();
    }

    public boolean canUserLogin(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(User::canLogin).orElse(false);
    }

    public boolean canUserLogin(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(User::canLogin).orElse(false);
    }

    public boolean isUserAdmin(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(User::isAdmin).orElse(false);
    }
}
