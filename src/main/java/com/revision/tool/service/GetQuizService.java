package com.revision.tool.service;

import com.revision.tool.controller.GetQuestionsController;
import com.revision.tool.controller.QuizController;
import com.revision.tool.dao.QuizRepo;
import com.revision.tool.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

@Service
public class GetQuizService {

    @Autowired
    private QuizRepo repo;


    public List<GetQuestionsController.QuizTitleDTO> get(Client cli) {
        List<Quiz> quizzes = repo.findByOwner(cli);

        // Use a stream to map each Quiz entity to a QuizTitleDTO
        return quizzes.stream()
                .map(quiz -> new GetQuestionsController.QuizTitleDTO(quiz.getId(), quiz.getTitle(),quiz.getDifficulty()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QuizController.UserFinalQuestions> getQuestions(Long quizId, UserPrinciple currentUser) {
        var quiz = repo.findById(quizId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Quiz not found"));

        var questions = quiz.getQuestions(); // now inside TX, so initialized

        var out = new java.util.ArrayList<QuizController.UserFinalQuestions>();
        for (var q : questions) {
            var dto = new QuizController.UserFinalQuestions();
            dto.setQuestionText(q.getQuestionText());

            var choiceDtos = new java.util.ArrayList<QuizController.ChoiceDTO>();
            for (var c : q.getChoices()) {                 // this is likely LAZY too
                var cd = new QuizController.ChoiceDTO();
                cd.setChoiceText(c.getChoiceText());
                cd.setCorrect(c.isCorrect());
                choiceDtos.add(cd);
            }
            dto.setChoices(choiceDtos);
            out.add(dto);
        }
        return out;
    }
}
