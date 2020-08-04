package com.udemy.data.jpa.dao;

import com.udemy.data.jpa.models.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductDao extends CrudRepository<Product, Long> {

    public List<Product> findByNameLikeIgnoreCase(String term);
}
