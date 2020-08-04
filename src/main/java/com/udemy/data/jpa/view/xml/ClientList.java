package com.udemy.data.jpa.view.xml;

import com.udemy.data.jpa.models.entities.Client;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "clients")
public class ClientList {
    @XmlElement(name = "client")
    public List<Client> clientes;

    public List<Client> getClientes(){
        return clientes;
    }
}
