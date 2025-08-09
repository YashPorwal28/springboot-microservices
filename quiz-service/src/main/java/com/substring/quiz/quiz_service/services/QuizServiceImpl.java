package com.substring.quiz.quiz_service.services;

import com.substring.quiz.quiz_service.Dto.QuizDto;
import com.substring.quiz.quiz_service.Repository.QuizRepository;
import com.substring.quiz.quiz_service.controller.CategoryDto;
import com.substring.quiz.quiz_service.entity.Quiz;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuizServiceImpl implements QuizService {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(QuizServiceImpl.class);

  private final QuizRepository quizRepository;

  private final CategoryFeignService categoryFeignService;

  private final ModelMapper modelMapper;

  private final CategoryService categoryService;
  private final RestTemplate restTemplate;

  public QuizServiceImpl(
      QuizRepository quizRepository,
      CategoryFeignService categoryFeignService,
      ModelMapper modelMapper,
      CategoryService categoryService,
      RestTemplate restTemplate) {
    this.quizRepository = quizRepository;
    this.categoryFeignService = categoryFeignService;
    this.modelMapper = modelMapper;
    this.categoryService = categoryService;
    this.restTemplate = restTemplate;
  }

  @Override
  public QuizDto create(QuizDto quizDto) {
    Quiz quiz = modelMapper.map(quizDto, Quiz.class);

    String url = "http://localhost:9091/api/v1/categories/" + quizDto.getCategoryId();
    logger.info(url);
    CategoryDto categoryDto = restTemplate.getForObject(url, CategoryDto.class);
    Quiz savedQuiz = quizRepository.save(quiz);
    QuizDto quizDto1 = modelMapper.map(savedQuiz, QuizDto.class);
    quizDto1.setCategory(categoryDto);
    return quizDto1;
  }

  @Override
  public QuizDto update(String quizId, QuizDto quizDto) {
    Quiz quiz =
        quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException(("Quiz not found")));
    quiz.setDescription(quizDto.getDescription());
    quiz.setTitle(quizDto.getTitle());
    Quiz savedQuiz = quizRepository.save(quiz);
    return modelMapper.map(savedQuiz, QuizDto.class);
  }

  @Override
  public void delete(String quizId) {
    Quiz quiz =
        quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException(("Quiz not found")));
    quizRepository.delete(quiz);
  }

  @Override
  public List<QuizDto> findAll() {
    List<Quiz> quizList = quizRepository.findAll();
    // getting category of all quiz  using webclient

    List<QuizDto> quizDtos =
        quizList.stream()
            .map(
                (quiz) -> {
                  String catgoryId = quiz.getCategoryId();

                  // CategoryDto categoryDto  =
                  // this.categoryService.findByIdUsingWebClient(catgoryId);
                  // using feign client
                  CategoryDto categoryDto = categoryFeignService.findById(catgoryId);
                  QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
                  quizDto.setCategory(categoryDto);
                  return quizDto;
                })
            .toList();

    return quizDtos;
  }

  @Override
  public QuizDto findById(String quizId) {
    Quiz quiz =
        quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException(("Quiz not found")));
    QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
    String categoryId = quiz.getCategoryId();
    String url = "http://localhost:9091/api/v1/categories/" + categoryId;
    logger.info(url);
    CategoryDto categoryDto = restTemplate.getForObject(url, CategoryDto.class);
    quizDto.setCategory(categoryDto);
    return quizDto;
  }

  @Override
  public List<QuizDto> findByCategoryId(String categoryId) {
    List<Quiz> quizList = quizRepository.findByCategoryId(categoryId);
    List<QuizDto> quizDtosList =
        quizList.stream()
            .map(
                (quiz -> {
                  CategoryDto categoryDto = categoryFeignService.findById(quiz.getCategoryId());
                  QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
                  quizDto.setCategory(categoryDto);
                  return quizDto;
                }))
            .toList();
    return quizDtosList;
  }
}
