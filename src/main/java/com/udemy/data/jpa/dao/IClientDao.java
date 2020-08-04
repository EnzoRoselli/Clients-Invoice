package com.udemy.data.jpa.dao;

import com.udemy.data.jpa.models.entities.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface IClientDao extends PagingAndSortingRepository<Client, Long> {

    @Query("select c from Client c " +
            "left join fetch c.invoices i " +
            "where c.id = ?1")
    public Client fetchByIdWithInvoices(Long id);
}
