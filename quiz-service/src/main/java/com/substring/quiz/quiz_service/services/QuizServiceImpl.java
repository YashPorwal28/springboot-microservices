package com.substring.quiz.quiz_service.services;

import com.substring.quiz.quiz_service.Dto.QuizDto;
import com.substring.quiz.quiz_service.Repository.QuizRepository;
import com.substring.quiz.quiz_service.controller.CategoryDto;
import com.substring.quiz.quiz_service.entity.Quiz;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(QuizServiceImpl.class);

    private final QuizRepository quizRepository;

    private final ModelMapper modelMapper;

    private final WebClient webClient;
    private final RestTemplate restTemplate;

    public QuizServiceImpl(RestTemplate restTemplate, WebClient webClient, ModelMapper modelMapper, QuizRepository quizRepository) {
        this.restTemplate = restTemplate;
        this.webClient = webClient;
        this.modelMapper = modelMapper;
        this.quizRepository = quizRepository;
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
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(()-> new RuntimeException(("Quiz not found")));
        quiz.setDescription(quizDto.getDescription());
        quiz.setTitle(quizDto.getTitle());
        Quiz savedQuiz = quizRepository.save(quiz);
        return modelMapper.map(savedQuiz, QuizDto.class);
    }

    @Override
    public void delete(String quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(()-> new RuntimeException(("Quiz not found")));
        quizRepository.delete(quiz);
    }

    @Override
    public List<QuizDto> findAll() {
        List<Quiz> quizList = quizRepository.findAll();
        // getting category of all quiz  using webclient

        List<QuizDto> quizDtos = quizList.stream().map((quiz)->{

            String catgoryId = quiz.getCategoryId();


          CategoryDto categoryDto =  this.webClient.get().uri("/api/v1/categories/" + catgoryId).retrieve().bodyToMono(CategoryDto.class).block();
          QuizDto quizDto = modelMapper.map(quiz, QuizDto.class);
            quizDto.setCategory(categoryDto);
            return quizDto;
        }).toList();
//        return quizList.stream().map((quiz)-> modelMapper.map(quiz, QuizDto.class)).toList();
        return quizDtos;
    }

    @Override
    public QuizDto findById(String quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(()-> new RuntimeException(("Quiz not found")));
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
        return quizList.stream().map((quiz)-> modelMapper.map(quiz, QuizDto.class)).toList();
    }
}
