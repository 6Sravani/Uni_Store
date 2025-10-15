package com.uni_store.store.Security;

import com.uni_store.store.User.UserResponseDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("api/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody UserRegistrationDto registrationDto)
    {
        UserResponseDto createdUser=authService.register(registrationDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("api/users/{id}")
                .buildAndExpand(createdUser.getId()).toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse response)
    {
        try{
            Map<String,String> tokens = authService.login(loginRequestDto);
            String accessToken = tokens.get("accessToken");
            String refreshToken = tokens.get("refreshToken");

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/api/auth"); // Only sent to auth-related endpoints
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // Set expiration (e.g., 7 days in seconds)

            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(new LoginResponseDto(accessToken));
        }catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletResponse response){
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        try{
            String newAccessToken = authService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(new LoginResponseDto(newAccessToken));
        }catch (JwtException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }
    }
}
