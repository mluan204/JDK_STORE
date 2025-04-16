package com.example.Backend_IE303.service;

import com.example.Backend_IE303.entity.AuthenticationResponse;
import com.example.Backend_IE303.entity.UserAccount;
import com.example.Backend_IE303.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class AuthenticationService {
    @Autowired
    private UserAccountRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserAccount request){
        if (repository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("Nguoi dung da ton tai");
        }

        UserAccount user = new UserAccount();
        user.setHo(request.getHo());
        user.setTen(request.getTen());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setDate_joined(new Date(System.currentTimeMillis()));

        user = repository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(UserAccount request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserAccount user = repository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }
}
