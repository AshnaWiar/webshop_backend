package org.example.core;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.example.api.product.ProductRegistration;
import org.example.api.product.ProductRepresentation;

import javax.persistence.*;


@Entity
@Table(name = "product")
@NamedQueries({
        @NamedQuery(
                name = "org.example.core.Product.findAll",
                query = "SELECT p FROM Product p"
        ),
        @NamedQuery(
                name = "org.example.core.Product.findByCategory",
                query = "SELECT p FROM Product p where p.category.id = :categoryId "
        ),
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;

    @Column(name = "image")
    public String image;

    @Column(name = "brand")
    public String brand;

    @Column(name = "title")
    public String title;

    @Column(name = "price")
    public Double price;

    @Column(name = "spec")
    public String spec;

    @Column(name = "description")
    public String description;

    @Column(name = "descriptionshort")
    public String descriptionShort;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    public Category category;

    public Product() {
    }

    public Product(String id) {
        this.id = id;
    }

    public Product(String id, String image, String brand, String title, double price, String spec, String description, String descriptionShort) {
        this.id = id;
        this.image = image;
        this.brand = brand;
        this.title = title;
        this.price = price;
        this.spec = spec;
        this.description = description;
        this.descriptionShort = descriptionShort;
    }

    public static Product createFromRegistration(ProductRegistration registration) {
        return new Product(
                null,
                registration.image,
                registration.brand,
                registration.title,
                registration.price,
                registration.spec,
                registration.description,
                registration.descriptionShort
        );
    }

    public void updateFromRepresentation(ProductRepresentation productRepresentation) {
        this.id = productRepresentation.id;
        this.image = productRepresentation.image;
        this.title = productRepresentation.title;
        this.price = productRepresentation.price;
        this.spec = productRepresentation.spec;
        this.description = productRepresentation.description;
        this.descriptionShort = productRepresentation.descriptionShort;
    }
}
