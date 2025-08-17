package com.revision.tool.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String choiceText;

    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id") // Specifies the foreign key column name
    private QuizQuestion question;
}