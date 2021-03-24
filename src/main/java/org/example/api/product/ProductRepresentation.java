package org.example.api.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.api.category.CategoryRelationRegistration;
import org.example.core.Product;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductRepresentation {

    @JsonProperty(required = true)
    @NotNull
    public String id;

    @JsonProperty
    @NotNull
    public String image;

    @JsonProperty
    @NotNull
    public String brand;

    @JsonProperty
    @NotNull
    public String title;

    @JsonProperty
    @NotNull
    public double price;

    @JsonProperty
    @NotNull
    public String spec;

    @JsonProperty
    @NotNull
    public String description;

    @JsonProperty
    @NotNull
    public String descriptionShort;

    @JsonProperty
    @NotNull
    @Valid
    public CategoryRelationRegistration category;

    public ProductRepresentation() {}

    public ProductRepresentation(
            @NotNull String id, @NotNull String image, @NotNull String brand, @NotNull String title,
            @NotNull double price, @NotNull String spec, @NotNull String description, @NotNull String descriptionShort,
            @NotNull CategoryRelationRegistration category
    ) {
        this.id = id;
        this.image = image;
        this.brand = brand;
        this.title = title;
        this.price = price;
        this.spec = spec;
        this.description = description;
        this.descriptionShort = descriptionShort;
        this.category = category;
    }

    public static ProductRepresentation createFromProduct(Product product) {
        return new ProductRepresentation(
                product.id,
                product.image,
                product.brand,
                product.title,
                product.price,
                product.spec,
                product.description,
                product.descriptionShort,
                new CategoryRelationRegistration(product.category.id)
        );
    }
}
