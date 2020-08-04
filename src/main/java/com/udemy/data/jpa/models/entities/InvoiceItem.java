package com.udemy.data.jpa.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "invoice_items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id") /*FK de table invoice_items con products*/
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Product product;

    public Double calculateImport(){
        return quantity.doubleValue() * product.getPrice();
    }
}
