package com.example.Backend_IE303.service;

import com.example.Backend_IE303.entity.UserAccount;
import com.example.Backend_IE303.exceptions.CustomException;
import com.example.Backend_IE303.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {
    @Autowired
    private UserAccountRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean changePass(String username, String newPass, String oldPass){
        UserAccount user = repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Khong tim thay user "+ username));
        if(!passwordEncoder.matches(oldPass, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPass));
        repository.save(user);
        return true;
    }
}
