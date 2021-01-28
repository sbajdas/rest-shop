package com.bajdas.rest_shop.client;

import com.bajdas.rest_shop.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

  @Autowired
  private ClientService clientService;

  @PostMapping("/addClient")
  ResponseEntity<Client> addClient(@RequestParam String name) {
    Client savedClient = clientService.addClient(name);
    return ResponseEntity.ok(savedClient);
  }
}
