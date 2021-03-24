package org.example.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.example.core.Account;
import org.example.core.Category;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

public class CategoryDAO extends AbstractDAO<Category> {

    public CategoryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Category> findById(String id) {
        return Optional.ofNullable(get(id));
    }

    @SuppressWarnings("unchecked")
    public List<Category> listAll() {
        return list((Query<Category>) namedQuery("org.example.core.Category.findAll"));
    }


    public Optional<Category> saveCategory(Category Category) {

        try {
            return Optional.of(persist(Category));
        } catch (Exception exception) {
            currentSession().clear();
        }

        return Optional.empty();
    }

    public void deleteCategory(String CategoryId) {
        currentSession().delete(findById(CategoryId).orElseThrow(NotFoundException::new));
    }
}
