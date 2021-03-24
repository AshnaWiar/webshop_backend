package org.example.core;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.example.api.category.CategoryRegistration;
import org.example.api.category.CategoryRepresentation;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(
                name = "org.example.core.Category.findAll",
                query = "SELECT c FROM Category c"
        ),
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;

    @Column(name = "image")
    public String image;

    @Column(name = "name")
    public String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonManagedReference
    public Set<Product> products;

    public Category() {
    }

    @SuppressWarnings("unused")
    public Category(String id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public Category(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public static Category createFromRegistration(CategoryRegistration registration) {
        return new Category(
                registration.image,
                registration.name
        );
    }

    public void updateFromRepresentation(CategoryRepresentation representation) {
        this.name = representation.name;
        this.image = representation.image;
    }
}
