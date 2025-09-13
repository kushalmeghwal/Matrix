package com.kushal.matrix.dtos;

import com.kushal.matrix.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private Role role;
}
