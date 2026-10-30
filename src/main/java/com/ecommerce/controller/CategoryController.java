package com.ecommerce.controller;

import com.ecommerce.entity.Category;
import com.ecommerce.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @RequestBody Category category) {

        Category savedCategory =
                categoryService.createCategory(category);

        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAllCategories()
        );
    }
}