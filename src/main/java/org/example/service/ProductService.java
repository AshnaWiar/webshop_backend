package org.example.service;

import org.example.api.product.ProductRepresentation;
import org.example.core.Product;
import org.example.db.ProductDAO;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

public class ProductService {

    private final ProductDAO productDAO;
    private final CategoryService categoryService;

    public ProductService(ProductDAO productDAO, CategoryService categoryService) {
        this.productDAO = productDAO;
        this.categoryService = categoryService;
    }

    public Product getProductById(String ProductId){
        return this.productDAO.findById(ProductId).orElseThrow(NotFoundException::new);
    }

    public List<Product> getAllProducts() {
        return this.productDAO.listAll();
    }

    public Product createProduct(Product product) {
        return this.productDAO.saveProduct(product).orElseThrow(NotFoundException::new);
    }

    public Product saveProduct(ProductRepresentation productRepresentation) {
        Product product = this.productDAO.findById(productRepresentation.id).orElseThrow(NotFoundException::new);
        product.updateFromRepresentation(productRepresentation);
        product.category = this.categoryService.getCategoryById(product.category.id);

        return this.productDAO.saveProduct(product).orElseThrow(BadRequestException::new);
    }

    public void deleteProduct(String ProductId) {
        this.productDAO.deleteProduct(ProductId);
    }

    public List<Product> getProductByCategory(String categoryId) {
        return this.productDAO.findByCategory(categoryId);
    }
}
