package org.example.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.api.category.CategoryRelationRegistration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class ProductRegistration {

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
}
