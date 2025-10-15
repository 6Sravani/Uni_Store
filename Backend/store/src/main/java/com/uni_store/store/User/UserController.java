package com.uni_store.store.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateUserProfile(
            Authentication authentication,
            @Valid @RequestBody UserUpdateDto updateDto) {

        String userEmail = authentication.getName();

        UserResponseDto updatedUser = userService.updateUserProfile(userEmail, updateDto);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();
        UserResponseDto userDto = userService.getUserByEmailAsDto(userEmail);

        return ResponseEntity.ok(userDto);
    }
}
