package org.example.db;

import org.example.core.Product;
import io.dropwizard.hibernate.AbstractDAO;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

public class ProductDAO extends AbstractDAO<Product> {

    public ProductDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(get(id));
    }

    @SuppressWarnings("unchecked")
    public List<Product> findByCategory(String categoryId) {
        return list((Query<Product>) namedQuery("org.example.core.Product.findByCategory")
                .setParameter("categoryId", categoryId)
        );
    }

    @SuppressWarnings("unchecked")
    public List<Product> listAll() {
        return list((Query<Product>) namedQuery("org.example.core.Product.findAll"));
    }


    public Optional<Product> saveProduct(Product Product) {

        try {
            return Optional.of(persist(Product));
        } catch (Exception exception) {
            currentSession().clear();
        }

        return Optional.empty();
    }

    public void deleteProduct(String ProductId) {
        currentSession().delete(findById(ProductId).orElseThrow(NotFoundException::new));
    }

}
