package com.uni_store.store.User;

import com.uni_store.store.Admin.AdminUserUpdateDto;
import com.uni_store.store.Product.PageDto;
import com.uni_store.store.exception.EmailAlreadyExistsException;
import com.uni_store.store.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto updateUserProfile(String email, UserUpdateDto userUpdateDto) {
        User user = getUserByEmailOrThrow(email);

        String newEmail = userUpdateDto.getEmail();
        if(newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail())) {
            if(userExists(newEmail)) {
                throw new EmailAlreadyExistsException(newEmail+"Email already exists");
            }
            user.changeEmail(newEmail);
        }
        String newPassword = userUpdateDto.getPassword();
        if(newPassword != null&& !newPassword.isBlank()) {
            user.changePassword(passwordEncoder.encode(newPassword));
        }

        userMapper.updateUserFromDto(userUpdateDto, user);

        return userMapper.userToUserResponseDto(user);
    }

    public void deleteUser(Long id) {
        User userToDelete = getUserByIdOrThrow(id);
        userRepository.delete(userToDelete);
    }

    public UserResponseDto updateUserByAdmin(Long id, AdminUserUpdateDto updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userMapper.updateUserFromAdminDto(updateDto, user);

        return userMapper.userToUserResponseDto(user);
    }

    public UserResponseDto getUserById(Long id) {
        var user=userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User with id "+id+" not found"));
        return userMapper.userToUserResponseDto(user);
    }
    private User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User with id "+id+" not found"));
    }
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User with email "+email+" not found"));
    }
    public UserResponseDto getUserByEmailAsDto(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::userToUserResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public UserResponseDto deactivateUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.deactivate();
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }

    public UserResponseDto activateUser(Long id) {
        User user = getUserByIdOrThrow(id);
        user.activate();
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }

    public UserResponseDto markEmailAsVerified(Long id) {
        User user = getUserByIdOrThrow(id);
        user.verifyEmail();
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }

    public UserResponseDto markEmailAsUnverified(Long id) {
        User user = getUserByIdOrThrow(id);
        user.unVerifyEmail();
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }
    public UserResponseDto changePassword(Long id, String password) {
        User user = getUserByIdOrThrow(id);
        user.changePassword(password);
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }

    public UserResponseDto promoteToAdmin(Long id) {
        User user = getUserByIdOrThrow(id);
        user.promoteToAdmin();
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }

    public UserResponseDto demoteToCustomer(Long id) {
        User user = getUserByIdOrThrow(id);
        user.demoteToCustomer();
        userRepository.save(user);
        return userMapper.userToUserResponseDto(user);
    }

    public PageDto<UserResponseDto> getAllActiveUsers(Pageable pageable) {

        Page<User> userPage = userRepository.findByIsActiveTrue(pageable);

        List<UserResponseDto> dtoList = userPage.getContent().stream()
                .map(userMapper::userToUserResponseDto)
                .toList();

        return new PageDto<>(
                dtoList,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    public PageDto<UserResponseDto> getAllUsers(Pageable pageable) {

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> dtoList = userPage.getContent().stream()
                .map(userMapper::userToUserResponseDto)
                .toList();

        return new PageDto<>(
                dtoList,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    public List<UserResponseDto> getUsersByRole(UserRole role) {
        List<User> roleBasedEntities = userRepository.findByRole(role);
        return roleBasedEntities.stream()
                .map(userMapper::userToUserResponseDto)
                .toList();
    }

    public List<UserResponseDto> getUsersByEmailVerifiedStatus(Boolean emailVerified) {
        List<User> emailVerifiedStatusEntities = userRepository.findByEmailVerified(emailVerified);
        return emailVerifiedStatusEntities.stream()
                .map(userMapper::userToUserResponseDto)
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
