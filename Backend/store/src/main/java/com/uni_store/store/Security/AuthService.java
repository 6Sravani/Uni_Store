package com.uni_store.store.Security;

import com.uni_store.store.User.*;
import com.uni_store.store.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    @Transactional
    public UserResponseDto register(UserRegistrationDto registrationDto){
        if(userRepository.existsByEmail(registrationDto.getEmail())){
            throw  new EmailAlreadyExistsException(registrationDto.getEmail()+" already exists");
        }
        User user = userMapper.userRegistrationDtoToUser(registrationDto);
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.userToUserResponseDto(savedUser);
    }

    public Map<String,String> login(LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.email(),
                        loginRequestDto.password()
                )
        );
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        return Map.of("accessToken",accessToken,"refreshToken",refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {

        Jwt parsedJwt = jwtService.parseToken(refreshToken);
        if (parsedJwt.isExpired()) {
            throw new BadCredentialsException("Refresh token has expired");
        }

        String userEmail = parsedJwt.getUsername();

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        return jwtService.generateAccessToken(userDetails);
    }
}
