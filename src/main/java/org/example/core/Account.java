package org.example.core;

import org.example.api.account.AccountRegistration;
import org.example.api.account.AccountRepresentation;
import org.example.enums.AccountRole;

import javax.persistence.*;
import java.security.Principal;
import java.util.Optional;

@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(
                name = "org.example.core.Account.findAll",
                query = "SELECT a FROM Account a"
        ),
        @NamedQuery(
                name = "org.example.core.Account.findByToken",
                query = "SELECT a FROM Account a where a.jwtToken = :jwtToken"
        ),
        @NamedQuery(
                name = "org.example.core.Account.findByUsername",
                query = "SELECT a FROM Account a where a.username = :username "
        )
})
public class Account implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;

    @Column(name = "role", unique = true)
    public String role;

    @Column(name = "username", unique = true)
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "firstname", unique = true)
    public String firstName;

    @Column(name = "lastname", unique = true)
    public String lastname;

    @Column(name = "streetName")
    public String streetName;

    @Column(name = "houseNumber")
    public String houseNumber;

    @Column(name = "postalCode")
    public String postalCode;

    @Column(name = "city")
    public String city;

    @Column(name = "jwttoken")
    public String jwtToken;

    public Account() {
    }

    public Account(
            String id,
            String role,
            String username,
            String password,
            String firstName,
            String lastname,
            String streetName,
            String houseNumber,
            String postalCode,
            String city,
            String jwtToken
    ) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastname = lastname;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.jwtToken = jwtToken;
    }

    public static Account createFromRegistration(AccountRegistration registration) {
        return new Account(
                null,
                AccountRole.USER,
                registration.username,
                registration.password,
                registration.firstName,
                registration.lastName,
                registration.streetName,
                registration.houseNumber,
                registration.postalCode,
                registration.city,
                null
        );
    }

    public static Account createFromRepresentation(AccountRepresentation accountRepresentation) {
        return new Account(
                accountRepresentation.id,
                accountRepresentation.role,
                accountRepresentation.username,
                accountRepresentation.password,
                accountRepresentation.firstName,
                accountRepresentation.lastName,
                accountRepresentation.streetName,
                accountRepresentation.houseNumber,
                accountRepresentation.postalCode,
                accountRepresentation.city,
                null
        );
    }

    public void updateFromRepresentation(AccountRepresentation representation) {
        this.firstName = representation.firstName;
        this.lastname = representation.lastName;
        this.username = representation.username;
        this.password = Optional.ofNullable(representation.password).orElse(this.password);
        this.streetName = representation.streetName;
        this.city = representation.city;
        this.houseNumber = representation.houseNumber;
        this.role = representation.role;
        System.out.println(this.password);
    }

    @Override
    public String getName() {
        return String.format("%s %s", firstName, lastname);
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
