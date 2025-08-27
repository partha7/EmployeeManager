package com.demo.employeemanager.services;

import com.demo.employeemanager.exceptions.ResourceNotFoundException;
import com.demo.employeemanager.models.dtos.*;
import com.demo.employeemanager.models.entities.UserEntity;
import com.demo.employeemanager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserDto createUser(SignUpDto signUpDto) {
        Optional<UserEntity> user = userRepository.findUserByEmail(signUpDto.getEmail());
        if (user.isPresent()) {
            throw new BadCredentialsException("User with email: "+signUpDto.getEmail()+" already exists");
        }
        else {
            UserEntity toBeCreatedUser = modelMapper.map(signUpDto, UserEntity.class);
            toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));
            return modelMapper.map(userRepository.save(toBeCreatedUser), UserDto.class);
        }
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<UserEntity> user = userRepository.findUserByEmail(loginRequestDto.getEmail());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with email: "+ loginRequestDto.getEmail()+ "not found");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        return new LoginResponseDto(userEntity.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refresh(RefreshRequestDto refreshRequestDto) {
        Long userId = jwtService.generateUserIdFromToken(refreshRequestDto.getRefreshToken());
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id: "+ userId+ "not found"));
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        return new LoginResponseDto(userEntity.getId(), accessToken, refreshToken);
    }
}
