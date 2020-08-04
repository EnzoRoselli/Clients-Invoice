package com.udemy.data.jpa.dao;

import com.udemy.data.jpa.models.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<User, Long> {

    public User findByUsername(String username);
}
