package com.bajdas.restshop.client;

import com.bajdas.restshop.model.Client;
import com.bajdas.restshop.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ClientService {
  @Autowired
  private ClientRepository clientRepository;

  Client addClient(String name) {
    var client = new Client();
    client.setName(name);
    return clientRepository.save(client);
  }
}
