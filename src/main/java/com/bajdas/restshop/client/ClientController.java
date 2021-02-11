package com.bajdas.restshop.client;

import com.bajdas.restshop.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

  private ClientService clientService;

  @Autowired
  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @PostMapping("/add")
  public ResponseEntity<Client> addClient(@RequestParam String name) {
    Client savedClient = clientService.addClient(name);
    return ResponseEntity.ok(savedClient);
  }
}
