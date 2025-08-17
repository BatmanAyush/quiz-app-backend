package com.revision.tool.service;

import com.revision.tool.controller.GetQuestionsController;
import com.revision.tool.controller.QuizController;
import com.revision.tool.dao.QuizRepo;
import com.revision.tool.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .map(quiz -> new GetQuestionsController.QuizTitleDTO(quiz.getId(), quiz.getTitle()))
                .collect(Collectors.toList());
    }

    public List<QuizController.UserFinalQuestions> getQuestions(Long quizId, UserPrinciple currentUser) {
       List<QuizController.UserFinalQuestions>finalQuestions = new ArrayList<>();

        Client cli = currentUser.getUser();
       Optional<Quiz> quiz = repo.findById(quizId);
       List<QuizQuestion> quizQuestions = quiz.get().getQuestions();
       List<QuizController.UserFinalQuestions>userFinalQuestionsList = new ArrayList<>();
       for(int i = 0;i<quizQuestions.size();i++){
           QuizController.UserFinalQuestions userQuestions = new QuizController.UserFinalQuestions();
           userQuestions.setQuestionText(quizQuestions.get(i).getQuestionText());
           List<Choice> choices = quizQuestions.get(i).getChoices();
           List<QuizController.ChoiceDTO>choiceDTOList=new ArrayList<>();
           for(int j = 0;j<choices.size();j++){
               QuizController.ChoiceDTO dto = new QuizController.ChoiceDTO();
               Choice choice = choices.get(j);
               dto.setChoiceText(choice.getChoiceText());
               dto.setCorrect(choice.isCorrect());
               choiceDTOList.add(dto);
           }
           userQuestions.setChoices(choiceDTOList);
           userFinalQuestionsList.add(userQuestions);
       }
       return userFinalQuestionsList;
    }
}
