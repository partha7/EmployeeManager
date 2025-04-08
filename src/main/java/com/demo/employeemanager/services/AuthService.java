package com.demo.employeemanager.services;

import com.demo.employeemanager.exceptions.ResourceNotFoundException;
import com.demo.employeemanager.models.dtos.LoginDto;
import com.demo.employeemanager.models.dtos.SignUpDto;
import com.demo.employeemanager.models.dtos.UserDto;
import com.demo.employeemanager.models.entities.UserEntity;
import com.demo.employeemanager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public String login(LoginDto loginDto) {
        Optional<UserEntity> user = userRepository.findUserByEmail(loginDto.getEmail());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with email: "+ loginDto.getEmail()+ "not found");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        return jwtService.generateToken(userEntity);
    }
}
