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
            System.out.println("Error searching products: " + e.getMessage());
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
            return productDao.create(product);
        }
        catch(Exception e) {
            System.err.println("Error adding product: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while searching for products.");
        }
    }

    @PutMapping("{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateProduct(@PathVariable int productId, @RequestBody Product product) {
        try {  //CRUD U = update == put
            Product existingProduct = productDao.getById(productId);
                productDao.update(productId, product);
      } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while searching for products.");
        }
    }

    @DeleteMapping("{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(@PathVariable int productId) {
        try {
            var product = productDao.getById(productId);
            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            productDao.delete(productId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while searching for products.");
        }
    }
}
