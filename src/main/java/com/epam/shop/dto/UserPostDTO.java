package com.epam.shop.dto;


import com.epam.shop.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.io.Serializable;

@Data
@EqualsAndHashCode
public class UserPostDTO implements Serializable {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;
    private List<OrderDTO> orders;


}
