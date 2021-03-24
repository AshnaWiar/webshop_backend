package org.example.api.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Credentials {

    @JsonProperty
    @NotNull
    public String username;

    @JsonProperty
    @NotNull
    public String password;
}
