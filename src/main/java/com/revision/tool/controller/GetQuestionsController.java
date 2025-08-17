package com.revision.tool.controller;

import com.revision.tool.model.Client;
import com.revision.tool.model.Quiz;
import com.revision.tool.model.UserPrinciple;
import com.revision.tool.service.GetQuizService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("*")
public class GetQuestionsController {

    @Autowired
    private GetQuizService getQuizService;

    @GetMapping("/getTitle")
    @CrossOrigin("*")
    public List<QuizTitleDTO> getUserTitle(@AuthenticationPrincipal UserPrinciple currentUser) {
        Client cli = currentUser.getUser();
        return getQuizService.get(cli);
    }
    @GetMapping("/{quizId}/getQuestions")
    @CrossOrigin("*")
    public List<QuizController.UserFinalQuestions>getQuestions(@PathVariable Long quizId, @AuthenticationPrincipal UserPrinciple currentUser){
            return getQuizService.getQuestions(quizId,currentUser);
    }

    // In GetQuestionsController.java
    @Data
    @AllArgsConstructor // Convenient for creating the object
    public static class QuizTitleDTO {
        private Long id;
        private String title;
    }
}
