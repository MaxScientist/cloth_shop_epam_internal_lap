package com.epam.shop.service.impl;

import com.epam.shop.entity.User;
import com.epam.shop.repository.UserRepository;
import com.epam.shop.service.GenericServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl extends GenericServiceImpl<User, Integer, UserRepository> {


    public void updateById(int id, User user) {
        User oldUser = super.findById(id);
        oldUser.setUsername(user.getUsername());
        oldUser.setFirstname(user.getFirstname());
        oldUser.setLastname(user.getLastname());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setPhoneNumber(user.getPhoneNumber());
        oldUser.setOrders(user.getOrders());
        super.save(oldUser);
    }

    public User findUserByUserName(String userName) {
        return super.r.findByUsername(userName);
    }
}
