package com.substring.quiz.quiz_service.services;

import com.substring.quiz.quiz_service.Dto.QuizDto;

import java.util.List;

public interface QuizService {

    QuizDto create(QuizDto quizDto);
    QuizDto update (String quizId, QuizDto quizDto);
    void delete( String quizId);
    List<QuizDto> findAll();
    QuizDto findById(String quizId);
    List<QuizDto> findByCategoryId(String categoryId);

}
