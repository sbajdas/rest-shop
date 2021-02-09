package com.bajdas.restshop.client;

import com.bajdas.restshop.model.Client;
import com.bajdas.restshop.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ClientService {
  private ClientRepository clientRepository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  Client addClient(String name) {
    var client = new Client();
    client.setName(name);
    return clientRepository.save(client);
  }
}
