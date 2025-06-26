package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin("*")
public class ProductsController {
    private final ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Product> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice, //nulls
                                @RequestParam(name="color", required = false) String color
                             ){
        try {
            return productDao.search(categoryId, minPrice, maxPrice, color);

        } catch (Exception e) {
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while searching for products.");
            }
        }
    }
    @GetMapping("{productId}")
    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int productId ){
        try {
            var product = productDao.getById(productId);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return product;
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while searching for products.");
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Product addProduct(@RequestBody Product product){
        try {
            Product productCreate = productDao.create(product);
            if(productCreate == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return productCreate;
        }
        catch(Exception e) {
            System.err.println("Error adding product: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create product due to an unexpected DAO issue.");
        }
    }

    @PutMapping("{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(@PathVariable int productId, @RequestBody Product product) {
        try {
            if (product.getProductId() != productId) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID in path does not match ID in request body.");
            }

            Product existingProduct = productDao.getById(productId);
            if (existingProduct == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + productId + " not found for update.");
            }

            productDao.update(productId, product);

            return productDao.getById(productId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while updating the product.");
        }
    }

    @DeleteMapping("{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(@PathVariable int productId) {
        try {
            var product = productDao.getById(productId);
            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + productId + " not found.");
            productDao.delete(productId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while deleting the product.");
        }
    }
}
