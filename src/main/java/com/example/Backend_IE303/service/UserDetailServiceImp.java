//package com.example.Backend_IE303.service;
//
//import com.example.Backend_IE303.repository.UserAccountRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailServiceImp implements UserDetailsService {
//    @Autowired
//    private UserAccountRepository userAccountRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!"));
//    }
//}
