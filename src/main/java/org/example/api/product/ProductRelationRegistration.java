package org.example.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ProductRelationRegistration {

    @JsonProperty
    @NotNull
    public String id;

    @JsonProperty
    @NotNull
    public String title;

    public ProductRelationRegistration() {
    }

    public ProductRelationRegistration(@NotNull String id, @NotNull String title) {
        this.id = id;
        this.title = title;
    }
}
