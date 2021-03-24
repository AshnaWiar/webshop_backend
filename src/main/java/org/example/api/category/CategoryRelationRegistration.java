package org.example.api.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CategoryRelationRegistration {

    @JsonProperty
    @NotNull
    public String id;

    public CategoryRelationRegistration() {
    }

    public CategoryRelationRegistration(String id) {
        this.id = id;
    }
}
