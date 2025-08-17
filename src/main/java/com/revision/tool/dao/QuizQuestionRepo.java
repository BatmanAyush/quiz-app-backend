package com.revision.tool.dao;

import com.revision.tool.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionRepo extends JpaRepository<QuizQuestion,Long> {

}
