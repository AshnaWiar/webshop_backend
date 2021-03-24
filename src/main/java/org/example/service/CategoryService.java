package org.example.service;

import org.example.core.Category;
import org.example.db.CategoryDAO;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class CategoryService {


    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public Category getCategoryById(String categoryId) throws NotFoundException {
        return this.categoryDAO.findById(categoryId).orElseThrow(NotFoundException::new);
    }

    public List<Category> getAllCategories() {
        return this.categoryDAO.listAll();
    }

    public Category createCategory(Category category) {
        return this.categoryDAO.saveCategory(category).orElseThrow(BadRequestException::new);
    }

    public Category saveCategory(Category category) {
        return this.categoryDAO.saveCategory(category).orElseThrow(BadRequestException::new);
    }

    public void deleteCategory(String categoryId) {
        this.categoryDAO.deleteCategory(categoryId);
    }
}
