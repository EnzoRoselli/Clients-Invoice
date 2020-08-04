package com.udemy.data.jpa.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@Builder
@AllArgsConstructor
public class Invoice implements Serializable {

    public Invoice(){
        invoiceItems = new ArrayList<InvoiceItem>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String description;
    private String observation;
    @Temporal(TemporalType.DATE)
    @Column(name="create_at")
    private Date createAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Client client;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    private List<InvoiceItem> invoiceItems;

    @PrePersist
    public void prePersist(){
        createAt = new Date();
    }

    public void addInvoiceItem(InvoiceItem invoiceItem){
        this.invoiceItems.add(invoiceItem);
    }

    public Double getTotal(){
        Double total = 0.0;

        for (int i = 0; i < invoiceItems.size(); i++){
            total += invoiceItems.get(i).calculateImport();
        }

        return total;
    }

    @XmlTransient
    public Client getClient(){
        return client;
    }
}
