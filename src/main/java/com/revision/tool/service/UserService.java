package com.revision.tool.service;

import com.revision.tool.model.Client;
import com.revision.tool.dao.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private ClientRepo repo;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public Client save(Client client){
        client.setPassword(encoder.encode(client.getPassword()));
        System.out.println("User Service");

        return repo.save(client);
    }

}
