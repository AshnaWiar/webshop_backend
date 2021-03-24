package org.example.api.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class CategoryRegistration {

    @JsonProperty
    @NotNull
    public String image;

    @JsonProperty
    @NotNull
    public String name;
}
