package com.substring.quiz.quiz_service.services;

import com.substring.quiz.quiz_service.controller.CategoryDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CategoryServiceWebClientImpl implements CategoryService {

  private final WebClient webClient;
  private final WebClient.Builder webClientBuilder;

  public CategoryServiceWebClientImpl( WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("http://CATEGORY-SERVICE").build() ;
    this.webClientBuilder = webClientBuilder;
  }

  @Override
  public CategoryDto findByIdUsingWebClient(String categoryId) {
    return this.webClient
        .get()
        .uri("/api/v1/categories/" + categoryId)
        .retrieve()
        .bodyToMono(CategoryDto.class)
        .block();
  }
}
