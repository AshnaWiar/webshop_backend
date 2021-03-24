package org.example.api.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AccountRegistration {

    @JsonProperty(required = true)
    @NotNull
    public String username;

    @JsonProperty(required = true)
    @NotNull
    public String password;

    @JsonProperty(required = true)
    @NotNull
    public String firstName;

    @JsonProperty(required = true)
    @NotNull
    public String lastName;

    @JsonProperty(required = true)
    @NotNull
    public String streetName;

    @JsonProperty(required = true)
    @NotNull
    public String houseNumber;

    @JsonProperty(required = true)
    @NotNull
    public String postalCode;

    @JsonProperty(required = true)
    @NotNull
    public String city;
}
