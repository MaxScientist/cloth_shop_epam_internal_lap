package com.epam.shop.dto;


import com.epam.shop.entity.Role;
import lombok.Data;

import java.util.List;
import java.io.Serializable;

@Data
public class UserPostDTO implements Serializable {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Role role;
    private List<OrderDTO> orders;


}
