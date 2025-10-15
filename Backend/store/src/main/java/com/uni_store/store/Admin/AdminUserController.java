package com.uni_store.store.Admin;

import com.uni_store.store.Product.PageDto;
import com.uni_store.store.User.UserResponseDto;
import com.uni_store.store.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageDto<UserResponseDto>> getAllUsers(Pageable pageable) {
        PageDto<UserResponseDto> userPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/activeUsers")
    public ResponseEntity<PageDto<UserResponseDto>> getAllActiveUsers(Pageable pageable) {
        PageDto<UserResponseDto> userDtos=userService.getAllActiveUsers(pageable);
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        var userDto= userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminUserUpdateDto updateDto) {

        UserResponseDto updatedUser = userService.updateUserByAdmin(id, updateDto);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
