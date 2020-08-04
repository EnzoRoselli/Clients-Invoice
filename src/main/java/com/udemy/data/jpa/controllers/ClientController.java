package com.udemy.data.jpa.controllers;

import com.udemy.data.jpa.models.entities.Client;
import com.udemy.data.jpa.services.interfaces.IClientService;
import com.udemy.data.jpa.services.interfaces.IUploadFileService;
import com.udemy.data.jpa.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

@Controller
@SessionAttributes("client")
public class ClientController {
    @Autowired
    private IClientService clientService;

    @Autowired
    private IUploadFileService uploadFileService;

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> seePhoto(@PathVariable String filename){

        Resource resource = null;
        try {
            resource = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash){
//        Client client = clientService.findById(id);

        Client client = clientService.fetchByIdWithInvoices(id);

        if (client == null){
            flash.addFlashAttribute("error", "El cliente no existe en la BBDD");
            return "redirect:/list";
        }
        model.addAttribute("client", client);
        model.addAttribute("title", "Detalle cliente: " + client.getName());

        return "ver";
    }

    @GetMapping("/api/list")
    public @ResponseBody List<Client> getAllClientsRest(){

        return clientService.findAll();
    }

    @GetMapping({"/list", "/"})
    public String getAllClients(@RequestParam(defaultValue = "0") Integer page, Model model, HttpServletRequest request){

        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Client> clients = clientService.findAll(pageRequest);

        PageRender<Client> pageRender = new PageRender<>("/list", clients);
        model.addAttribute("title", "Listado de clientes");
        model.addAttribute("clients", clients);
        model.addAttribute("page", pageRender);

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");

        if (securityContext.isUserInRole("ADMIN")){

        }

        return "list";
    }

    @GetMapping("/form")
    public String createClient(Model model){
        Client client = new Client();
        model.addAttribute("client", client);
        model.addAttribute("title", "Formularo de cliente");

        return "form";
    }

    @PostMapping("/form")
    public String saveClient(@Valid Client client, BindingResult result, Model model,
                             @RequestParam("file") MultipartFile photo, RedirectAttributes flash, SessionStatus status){
        if(result.hasErrors()){
            model.addAttribute("title", "Formularo de cliente");
            return "form";
        }

        if (!photo.isEmpty()){

            if ((client.getId() != null && client.getId() > 0) &&
                    client.getPhoto() != null && (client.getPhoto().length() > 0)){

                uploadFileService.delete(client.getPhoto());
            }

            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }

            flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'.");
            client.setPhoto(uniqueFilename);
        }

        String flashMessage = (client.getId() != null) ? "Cliente editado con exito." : "Cliente agregado con exito.";

        clientService.save(client);
        status.setComplete();
        flash.addFlashAttribute("success", flashMessage);
        return "redirect:/list";
    }

    @GetMapping("/form/{id}")
    public String editClient(@PathVariable Long id, RedirectAttributes flash, Model model){
        Client client = null;

        if (id > 0){
            client = clientService.findById(id);
            if (client == null){

                flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD.");
                return "redirect:/list";
            }
        }else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser cero.");
            return "redirect:/list";
        }
        model.addAttribute("client", client);
        model.addAttribute("title", "Editar cliente");
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash){
        if (id > 0){
            Client client = clientService.findById(id);
            clientService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito.");


            if (uploadFileService.delete(client.getPhoto())){
                flash.addFlashAttribute("info", "Foto " + client.getPhoto() + " eliminada con exito.");
            }

        }
        return "redirect:/list";
    }

    private boolean hasRole(String role){

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        if (context ==null || auth == null){
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();


        return authorities.contains(new SimpleGrantedAuthority(role));
    }

}
