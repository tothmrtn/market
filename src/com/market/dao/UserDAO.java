package com.market.dao;

import com.market.model.User;

public interface UserDAO {
    
    void addUser(User user);
    public boolean alreadyRegistered(String email);
    public User getUser(String name, String password);
    
}
