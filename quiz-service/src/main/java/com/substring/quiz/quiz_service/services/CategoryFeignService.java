package com.substring.quiz.quiz_service.services;

import com.substring.quiz.quiz_service.controller.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name ="CATEGORY-SERVICE")
public interface CategoryFeignService {

    // Declarative

    @GetMapping("/api/v1/categories")
    List<CategoryDto> findALl();

    @GetMapping("/api/v1/categories/{id}")
    CategoryDto findById(@PathVariable String id);

    @PostMapping("/api/v1/categories")
    CategoryDto create(@RequestBody CategoryDto categoryDto);

}
