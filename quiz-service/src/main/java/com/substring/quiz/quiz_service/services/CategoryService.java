package com.substring.quiz.quiz_service.services;

import com.substring.quiz.quiz_service.controller.CategoryDto;

public interface CategoryService {

  CategoryDto findByIdUsingWebClient(String categoryId);
}
