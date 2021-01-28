package com.bajdas.rest_shop.client;

import com.bajdas.rest_shop.model.Client;
import com.bajdas.rest_shop.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
  @Autowired
  private ClientRepository clientRepository;

  public Client addClient(String name) {
    var client = new Client();
    client.setName(name);
    return clientRepository.save(client);
  }
}
