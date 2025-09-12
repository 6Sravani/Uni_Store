package com.uni_store.store.User;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRegistrationDto registrationDto) {
        if(userExists(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user=userMapper.userRegistrationDtoToUser(registrationDto);

        User savedUser=userRepository.save(user);

        return userMapper.userToUserResponceDto(savedUser);
    }

    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        User user = getUserByIdOrThrow(id);

        String newEmail = userUpdateDto.getEmail();
        if(newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail())) {
            if(userExists(newEmail)) {
                throw new RuntimeException("Email already exists");
            }
            user.changeEmail(newEmail);
        }
        String newPassword = userUpdateDto.getPassword();
        if(newPassword != null&& !newPassword.isBlank() && !newPassword.equals(user.getPasswordHash())) {
            user.changePassword(newPassword);
        }

        userMapper.updateUserFromDto(userUpdateDto, user);

        return userMapper.userToUserResponceDto(user);
    }

    public void deleteUser(Long id) {
        User userToDelete = getUserByIdOrThrow(id);
        userRepository.delete(userToDelete);
    }

    public UserResponseDto getUserById(Long id) {
        var user=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        return userMapper.userToUserResponceDto(user);
    }
    private User getUserByIdOrThrow(Long id) {
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

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public UserResponseDto deactivateUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.deactivate();
        userRepository.save(user);
        return userMapper.userToUserResponceDto(user);
    }

    public UserResponseDto activateUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.activate();
        userRepository.save(user);
        return userMapper.userToUserResponceDto(user);
    }

    public UserResponseDto markEmailAsVerified(Long id) {
        User user = getUserByIdOrThrow(id);
        user.verifyEmail();
        userRepository.save(user);
        return userMapper.userToUserResponceDto(user);
    }

    public UserResponseDto markEmailAsUnverified(Long id) {
        User user = getUserByIdOrThrow(id);
        user.unVerifyEmail();
        userRepository.save(user);
        return userMapper.userToUserResponceDto(user);
    }
    public UserResponseDto changePassword(Long id, String password) {
        User user = getUserByIdOrThrow(id);
        user.changePassword(password);
        userRepository.save(user);
        return userMapper.userToUserResponceDto(user);
    }

    public UserResponseDto promoteToAdmin(Long id) {
        User user = getUserByIdOrThrow(id);
        user.promoteToAdmin();
        userRepository.save(user);
        return userMapper.userToUserResponceDto(user);
    }

    public UserResponseDto demoteToCustomer(Long id) {
        User user = getUserByIdOrThrow(id);
        user.demoteToCustomer();
        userRepository.save(user);
        return userMapper.userToUserResponceDto(user);
    }

    public List<UserResponseDto> getAllActiveUsers() {
        List<User> activeUserEntities =userRepository.findByIsActiveTrue();
        return activeUserEntities.stream()
                .map(userMapper::userToUserResponceDto)
                .toList();
    }

    public List<UserResponseDto> getUsersByRole(UserRole role) {
        List<User> roleBasedEntities = userRepository.findByRole(role);
        return roleBasedEntities.stream()
                .map(userMapper::userToUserResponceDto)
                .toList();
    }

    public List<UserResponseDto> getUsersByEmailVerifiedStatus(Boolean emailVerified) {
        List<User> emailVerifiedStatusEntities = userRepository.findByEmailVerified(emailVerified);
        return emailVerifiedStatusEntities.stream()
                .map(userMapper::userToUserResponceDto)
                .toList();
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
