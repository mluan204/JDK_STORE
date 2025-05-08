package com.example.Backend_IE303.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChangePassRequestDTO {
    private String username;
    private String new_pass;
    private String old_pass;
}
