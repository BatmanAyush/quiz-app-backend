package com.revision.tool.dao;

import com.revision.tool.model.Client;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClientRepo extends JpaRepository<Client, Integer> {
    Client findByName(String name);
    Optional<Client> findById(int id);
}