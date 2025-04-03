//package com.example.Backend_IE303.controller;
//
//import com.example.Backend_IE303.entity.AuthenticationResponse;
//import com.example.Backend_IE303.entity.UserAccount;
//import com.example.Backend_IE303.service.AuthenticationService;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/v1")
//public class AuthenticationController {
//    private final AuthenticationService authService;
//
//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserAccount request){
//        return ResponseEntity.ok(authService.register(request));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserAccount request){
//        return ResponseEntity.ok(authService.authenticate(request));
//    }
//}
