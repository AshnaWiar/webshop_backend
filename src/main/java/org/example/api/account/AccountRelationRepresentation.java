package org.example.api.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AccountRelationRepresentation {

    @JsonProperty
    @NotNull
    public String id;

    public AccountRelationRepresentation() {
    }

    public AccountRelationRepresentation(@NotNull String id) {
        this.id = id;
    }
}
