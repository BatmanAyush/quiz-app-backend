package com.revision.tool.dao;

import com.revision.tool.model.Client;
import com.revision.tool.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizRepo extends JpaRepository<Quiz,Long> {

    List<Quiz> findByOwner(Client owner);

}
