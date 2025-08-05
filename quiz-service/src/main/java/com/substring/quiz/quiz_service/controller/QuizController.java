package com.substring.quiz.quiz_service.controller;

import com.substring.quiz.quiz_service.Dto.QuizDto;
import com.substring.quiz.quiz_service.entity.Quiz;
import com.substring.quiz.quiz_service.services.QuizService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    private QuizService quizService;


    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

        @PostMapping
        public ResponseEntity<QuizDto> create(@RequestBody QuizDto quizDto){
            QuizDto quiz = quizService.create(quizDto);
            return new ResponseEntity<>(quiz , HttpStatus.CREATED);
        }

        @GetMapping
        public ResponseEntity<List<QuizDto>> getALlQuiz (){
             List<QuizDto> list = quizService.findAll();
             return new ResponseEntity<>(list, HttpStatus.OK);
        }

        @GetMapping("/category/{id}")
        public ResponseEntity<List<QuizDto>> findByCategory(@PathVariable("id") String categoryId){
            return new ResponseEntity<>(quizService.findByCategoryId(categoryId), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<QuizDto> findbyId (@PathVariable("id") String id){
        QuizDto quizDto  = quizService.findById(id);
        return new ResponseEntity<>(quizDto, HttpStatus.OK);
        }

        @PutMapping("/{id}")
        public  ResponseEntity<String> updateQuiz (@PathVariable("id") String id , @RequestBody QuizDto quizDto){
            QuizDto quizDto1 = quizService.update(id, quizDto);
            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        }



}
