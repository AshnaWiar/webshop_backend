package org.example.api.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.core.Account;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountRepresentation {

    @JsonProperty(required = true)
    @NotNull
    public String id;

    @JsonProperty(required = true)
    @NotNull
    public String role;

    @JsonProperty(required = true)
    @NotNull
    public String firstName;

    @JsonProperty(required = true)
    @NotNull
    public String lastName;

    @JsonProperty(required = true)
    @NotNull
    public String username;

    @JsonProperty(required = true)
    public String password;

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

    @JsonProperty(required = true)
    @NotNull
    public String jwtToken;

    public AccountRepresentation() {
    }

    public AccountRepresentation(
            @NotNull String id, @NotNull String role, @NotNull String username, @NotNull String firstName,
            @NotNull String lastName, @NotNull String streetName, @NotNull String houseNumber,
            @NotNull String postalCode, @NotNull String city, @NotNull String jwtToken
    ) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.jwtToken = jwtToken;
    }

    public static AccountRepresentation createFromAccount(Account account) {
        return new AccountRepresentation(
                account.id,
                account.role,
                account.username,
                account.firstName,
                account.lastname,
                account.streetName,
                account.houseNumber,
                account.postalCode,
                account.city,
                account.jwtToken
        );
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Account) {
            return ((Account) obj).id.equalsIgnoreCase(this.id);
        }

        if (obj instanceof AccountRepresentation) {
            return ((AccountRepresentation) obj).id.equalsIgnoreCase(this.id);
        }

        return false;
    }
}
