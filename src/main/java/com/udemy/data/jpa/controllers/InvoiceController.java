package com.udemy.data.jpa.controllers;

import com.udemy.data.jpa.models.entities.Client;
import com.udemy.data.jpa.models.entities.Invoice;
import com.udemy.data.jpa.models.entities.InvoiceItem;
import com.udemy.data.jpa.models.entities.Product;
import com.udemy.data.jpa.services.interfaces.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
public class InvoiceController {
    @Autowired
    private IClientService clientService;

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes flash){
//        Invoice invoice = clientService.findInvoiceById(id);

        Invoice invoice = clientService.fetchInvoiceByIdWithClientWithInvoiceItemsWithProduct(id);

        if (invoice == null){
            flash.addFlashAttribute("error", "La factura no existe en la BBDD.");
            return "redirect:/list";
        }
        model.addAttribute("invoice", invoice);
        model.addAttribute("title", "Factura: ".concat(invoice.getDescription()));

        return "invoice/ver";
    }

    @GetMapping("/form/{clientId}")
    public String createInvoice(@PathVariable Long clientId, Model model, RedirectAttributes flash){
        Client client = clientService.findById(clientId);
        if (client == null){
            flash.addFlashAttribute("error", "El cliente no existe en la BBDD.");
            return "redirect:/list";
        }

        Invoice invoice = new Invoice();
        invoice.setClient(client);
        model.addAttribute("invoice", invoice);
        model.addAttribute("title", "Crear factura");

        return "invoice/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Product> cargarProductos(@PathVariable String term){ /*@ResponseBody se usa para no cargar una vista, sino guardar en el body del response de la request la informacion, en este caso de tipo json*/
        return clientService.findByName(term);
    }

    @PostMapping("/form")
    public String saveInvoice(@Valid Invoice invoice, BindingResult result, Model model,
                              @RequestParam(value = "item_id[]", required = false) Long[] itemId,
                              @RequestParam(value = "quantity[]", required = false) Integer[] quantity,
                              RedirectAttributes flash, SessionStatus status){

        if (result.hasErrors()){
            model.addAttribute("title", "Crear Factura");
            return "invoice/form";
        }

        if (itemId == null || itemId.length == 0){
            model.addAttribute("title", "Crear Factura");
            model.addAttribute("error", "La factura debe tener al menos una linea");
            return "invoice/form";
        }

        for (int i=0; i < itemId.length; i++){
            Product product = clientService.findProductById(itemId[i]);

            InvoiceItem invoiceItem = InvoiceItem.builder().product(product).quantity(quantity[i]).build();

            invoice.addInvoiceItem(invoiceItem);
        }

        clientService.saveInvoice(invoice);
        status.setComplete();

        flash.addFlashAttribute("success", "Factura creada con exito.");

        return "redirect:/ver/" + invoice.getClient().getId();
    }

    @GetMapping("/eliminar/{id}")
    public String deleteInvoice(@PathVariable Long id, RedirectAttributes flash){
        Invoice invoice = clientService.findInvoiceById(id);

        if (invoice == null){
            flash.addFlashAttribute("error", "La factura no existe en la BBDD.");
            return "redirect:/list";
        }

        clientService.deleteInvoiceById(id);
        flash.addFlashAttribute("success", "Factura eliminada con exito");

        return "redirect:/ver/" + invoice.getClient().getId();
    }
}
