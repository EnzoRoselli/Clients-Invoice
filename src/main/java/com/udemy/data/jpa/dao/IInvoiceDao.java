package com.udemy.data.jpa.dao;

import com.udemy.data.jpa.models.entities.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IInvoiceDao extends CrudRepository<Invoice, Long> {

    @Query("select i from Invoice i " +
            "join fetch i.client c " +
            "join fetch i.invoiceItems it " +
            "join fetch it.product " +
            "where i.id = ?1")
    public Invoice fetchByIdWithClientWithInvoiceItemWithProduct(Long id);
}
