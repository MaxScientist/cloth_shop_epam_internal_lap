package com.epam.shop.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@ToString
@RequiredArgsConstructor
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name")
    private String username;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastname;
    @Column(name = "email")
    private String email;
    private String password;
    private String phoneNumber;

    @ManyToOne(cascade =  CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = false)
    @JoinColumn(name = "user_id")
    private List<Order> orders;
}
