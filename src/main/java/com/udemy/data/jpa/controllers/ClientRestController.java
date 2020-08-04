package com.udemy.data.jpa.controllers;

import com.udemy.data.jpa.services.interfaces.IClientService;
import com.udemy.data.jpa.view.xml.ClientList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientRestController {
    @Autowired
    private IClientService clientService;

    @GetMapping("/list")
    public ClientList getAllClients(){

        return new ClientList(clientService.findAll());
    }
}
