package com.revision.tool.service;


import com.revision.tool.controller.QuizController;
import com.revision.tool.dao.ChoiceRepo;
import com.revision.tool.dao.QuizQuestionRepo;
import com.revision.tool.dao.QuizRepo;
import com.revision.tool.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepo repo;

    @Autowired
    private ChoiceRepo choiceRepo;

    @Autowired
    private QuizQuestionRepo qrepo;

    public Quiz save(QuizController.CreateQuizRequest quizRequest, UserPrinciple currentUser) {
        Client cli = currentUser.getUser();
        Quiz q = new Quiz();
        q.setOwner(cli);
        q.setTitle(quizRequest.getTitle());
        return repo.save(q);
    }


    // In QuizService.java

    public boolean saveQuestion(Long quizId, List<QuizController.UserFinalQuestions> quizQuestions, UserPrinciple currentUser) {
        Quiz parentQuiz = repo.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        for (QuizController.UserFinalQuestions questionDto : quizQuestions) {
            QuizQuestion newQuestion = new QuizQuestion();
            newQuestion.setQuestionText(questionDto.getQuestionText());
            newQuestion.setQuiz(parentQuiz);
            qrepo.save(newQuestion);

            for (QuizController.ChoiceDTO choiceDto : questionDto.getChoices()) {
                Choice newChoice = new Choice();
                newChoice.setChoiceText(choiceDto.getChoiceText());
                newChoice.setCorrect(choiceDto.isCorrect());
                System.out.println(choiceDto.isCorrect());
                newChoice.setQuestion(newQuestion);
                choiceRepo.save(newChoice);
            }
        }
        return true;
    }
}
