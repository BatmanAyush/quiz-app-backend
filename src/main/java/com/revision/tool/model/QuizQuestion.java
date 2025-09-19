package com.revision.tool.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.Optional;

@Data
@Entity
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String questionText;

    @ManyToOne
    @JoinColumn(name = "quiz_id") // Specifies the foreign key column name
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Choice> choices;
}
