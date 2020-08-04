package com.udemy.data.jpa.services.interfaces;

import com.udemy.data.jpa.models.entities.Client;
import com.udemy.data.jpa.models.entities.Invoice;
import com.udemy.data.jpa.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClientService {
    public Page<Client> findAll(Pageable pageable);
    public List<Client> findAll();
    public void save(Client client);
    public Client findById(Long id);
    public void delete(Long id);
    public List<Product> findByName(String term);
    public void saveInvoice(Invoice invoice);
    public Product findProductById(Long id);
    public Invoice findInvoiceById(Long id);
    public void deleteInvoiceById(Long id);
    public Invoice fetchInvoiceByIdWithClientWithInvoiceItemsWithProduct(Long id);
    public Client fetchByIdWithInvoices(Long id);
}
