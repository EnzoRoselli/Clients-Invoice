package com.udemy.data.jpa.services;

import com.udemy.data.jpa.dao.IClientDao;
import com.udemy.data.jpa.dao.IInvoiceDao;
import com.udemy.data.jpa.dao.IProductDao;
import com.udemy.data.jpa.models.entities.Client;
import com.udemy.data.jpa.models.entities.Invoice;
import com.udemy.data.jpa.models.entities.Product;
import com.udemy.data.jpa.services.interfaces.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService implements IClientService {
    @Autowired
    private IClientDao clientDao;

    @Autowired
    private IProductDao productDao;

    @Autowired
    private IInvoiceDao invoiceDao;

    @Transactional(readOnly = true)
    @Override
    public Page<Client> findAll(Pageable pageable) {
        return clientDao.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return (List<Client>) clientDao.findAll();
    }

    @Transactional
    @Override
    public void save(Client client) {
        clientDao.save(client);
    }

    @Transactional(readOnly = true)
    @Override
    public Client findById(Long id) {
        return clientDao.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Client client = findById(id);
        clientDao.delete(client);
    }

    @Override
    public List<Product> findByName(String term) {
        return productDao.findByNameLikeIgnoreCase("%" + term + "%");
    }

    @Override
    @Transactional
    public void saveInvoice(Invoice invoice) {
        invoiceDao.save(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findProductById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice findInvoiceById(Long id) {
        return invoiceDao.findById(id).orElse(null);
    }

    @Override
    public void deleteInvoiceById(Long id) {
        invoiceDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice fetchInvoiceByIdWithClientWithInvoiceItemsWithProduct(Long id) {
        return invoiceDao.fetchByIdWithClientWithInvoiceItemWithProduct(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Client fetchByIdWithInvoices(Long id) {
        return clientDao.fetchByIdWithInvoices(id);
    }
}
