package com.substring.quiz.quiz_service.Repository;

import com.substring.quiz.quiz_service.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {
        List<Quiz> findByCategoryId(String categoryId);
}
