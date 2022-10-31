package com.market.dao;

import java.util.List;

public interface DAO<T> {
    
    List<T> getAll();
    int save(T t);
    int insert(T t);
    int update(T t);
    int delete(T t);

}
