package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.ChangePassRequestDTO;
import com.example.Backend_IE303.exceptions.ApiResponse;
import com.example.Backend_IE303.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserAccountController {
    @Autowired
    private UserAccountService service;

    @PostMapping("/change-pass")
    public ResponseEntity<ApiResponse> changePass (@RequestBody ChangePassRequestDTO dto){
        ApiResponse a = new ApiResponse(101, "Mat khau khong chinh xac");
        if(service.changePass(dto.getUsername(), dto.getNew_pass(), dto.getOld_pass())){
            a.setStatus(100);
            a.setMessage("Cap nhat thanh cong");
        }
        return ResponseEntity.ok(a);
    }
}
