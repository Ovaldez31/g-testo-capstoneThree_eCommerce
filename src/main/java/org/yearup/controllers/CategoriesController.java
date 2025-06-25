package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

    @RestController
    @RequestMapping("categories")
    @CrossOrigin("*")
    @PreAuthorize("hasRole('ADMIN')")
    public class CategoriesController {

        private final CategoryDao categoryDao;
        private final ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao){
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    } // inject the categoryDao and ProductDao

    @GetMapping("")// the endpoint
    public List<Category> getAllCategories() {
        try {
            return categoryDao.getAllCategories();
        } catch (Exception e) {
            System.out.println("Error retrieving all categories: " + e.getMessage());
            throw e;
        }
    }
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> listByCategoryId(@PathVariable int categoryId) {
        try {
            return productDao.listByCategoryId(categoryId);
    } catch (Exception e) {
        System.out.println("Error retrieving all categories: " + e.getMessage());
        throw e;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody Category category) {
        try {
            return categoryDao.create(category);
        } catch (ResponseStatusException rse) {
            throw rse;
        } catch (Exception e) {
            System.err.println("Error retrieving products for category ID " + category + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while retrieving products for the category.", e);
        }
    }
        @PutMapping("{categoryId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void update(@PathVariable int category_id, @RequestBody Category category) {
            try {
                categoryDao.update(category_id, category);
            } catch (ResponseStatusException rse) {
                throw rse;
            } catch (Exception e) {
                System.err.println("Error retrieving products for category ID " + category + ": " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred while retrieving products for the category.", e);
            }
        }

        @DeleteMapping("{categoryId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(@PathVariable int category_id) {
            categoryDao.delete(category_id);

    }
}
