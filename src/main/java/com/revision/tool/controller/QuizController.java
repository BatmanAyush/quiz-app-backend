package com.revision.tool.controller;

import com.revision.tool.model.*;
import com.revision.tool.service.QuizService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @CrossOrigin("*")
    @PostMapping("/saveTitle")
    public Quiz quizSave(@RequestBody CreateQuizRequest quizRequest, @AuthenticationPrincipal UserPrinciple currentUser){
        System.out.println("Entered Quiz Controller");
            return quizService.save(quizRequest,currentUser);
    }
    @CrossOrigin("*")
    @PostMapping("/{quizId}/saveQuestions")
    public boolean questionSave(@PathVariable Long quizId,@RequestBody List<UserFinalQuestions> quizQuestions,@AuthenticationPrincipal UserPrinciple currentUser){
        return quizService.saveQuestion(quizId,quizQuestions,currentUser);
    }

    @PostMapping("/{quizId}/delete")
    public boolean questionDelete(@PathVariable Long quizId,@AuthenticationPrincipal UserPrinciple currentUser){
            return quizService.handleDelete(quizId,currentUser);
    }
    @Data
    public static class CreateQuizRequest{
        private String title;
    }

    @Data
    public static class  UserFinalQuestions{
        private String questionText;
        private List<ChoiceDTO>choices;
    }
    @Data
    public static class ChoiceDTO{
       private String choiceText;
       private boolean correct;
    }
}

