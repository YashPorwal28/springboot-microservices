package com.substring.quiz.category.impl;


import com.substring.quiz.category.dto.CategoryDto;
import com.substring.quiz.category.entities.Category;
import com.substring.quiz.category.repository.CategoryRepository;
import com.substring.quiz.category.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory , CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(String categoryId, CategoryDto categoryDto) {
       Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException("Category not found"));
       category.setTitle(categoryDto.getTitle());
       category.setDescription(categoryDto.getDescription());
       category.setActive(categoryDto.isActive());
       Category savedCategory = categoryRepository.save(category);
       return modelMapper.map(savedCategory, CategoryDto.class);

    }

    @Override
    public CategoryDto getCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException("Category not found"));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> AllCategory = categoryRepository.findAll();
        return AllCategory.stream().map((category)-> modelMapper.map(category, CategoryDto.class)).toList();
    }
}
