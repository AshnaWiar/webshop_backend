package org.example.api.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.core.Category;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryRepresentation {

    @JsonProperty
    @NotNull
    public String id;

    @JsonProperty
    @NotNull
    public String image;

    @JsonProperty
    @NotNull
    public String name;


    public CategoryRepresentation() {
    }

    public CategoryRepresentation(String id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public static CategoryRepresentation createFromCategory(Category category) {
        return new CategoryRepresentation(
                category.id,
                category.image,
                category.name
        );
    }
}
