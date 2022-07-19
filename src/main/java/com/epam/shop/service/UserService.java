package com.epam.shop.service;

import com.epam.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService extends JpaRepository<User,Integer> {
}
