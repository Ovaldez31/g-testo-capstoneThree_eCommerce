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
    public class CategoriesController {

        private final CategoryDao categoryDao;
        private final ProductDao productDao;

        @Autowired
        public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
            this.categoryDao = categoryDao;
            this.productDao = productDao;
        }

        @GetMapping("")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("permitAll()")
        public List<Category> getAllCategories() {
            try {
                return categoryDao.getAllCategories();
            } catch (Exception e) {
                System.out.println("Error retrieving all categories: " + e.getMessage());
                throw e;
            }
        }

        @GetMapping("{categoryId}/products")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("permitAll()")
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
        @PreAuthorize("hasRole('ADMIN')")
        public Category create(@RequestBody Category category) {
            try {
                return categoryDao.create(category);
            } catch (Exception e) {
                System.out.println("Error adding categories: " + e.getMessage());
                throw e;
            }
        }

        @PutMapping("{categoryId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @PreAuthorize("hasRole('ADMIN')")
        public void update(@PathVariable int categoryId, @RequestBody Category category) {
            try {
                categoryDao.update(categoryId, category);
            } catch (Exception e) {
                System.out.println("Error updating categoryId: " + e.getMessage());
                throw e;
            }
        }

        @DeleteMapping("{categoryId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @PreAuthorize("hasRole('ADMIN')")
        public void delete(@PathVariable int categoryId) {
            try {
                var category = categoryDao.getById(categoryId);
                if (category == null)
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + categoryId + " not found.");
                categoryDao.delete(categoryId);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while deleting the category.");

            }
        }
    }
