package com.epam.shop.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@RequiredArgsConstructor
@Getter
@Table(name = "users")
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneNumber;

    @ManyToOne(cascade =  CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<Order> orders;
}
