package org.example.resources;

import io.dropwizard.hibernate.UnitOfWork;
import org.example.api.category.CategoryRegistration;
import org.example.api.category.CategoryRepresentation;
import org.example.api.product.ProductRepresentation;
import org.example.core.Category;
import org.example.enums.AccountRole;
import org.example.service.CategoryService;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/category")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GET
    @UnitOfWork
    public Response getAll() {
        return Response.ok().entity(
                this.categoryService.getAllCategories()
                        .stream()
                        .map(CategoryRepresentation::createFromCategory)
                        .collect(Collectors.toList())
        ).build();
    }

    @GET
    @Path("/{categoryId}")
    @UnitOfWork
    public Response get(@PathParam("categoryId") String categoryId) {
        return Response.ok(
                CategoryRepresentation.createFromCategory(
                        this.categoryService.getCategoryById(categoryId)
                )
        ).build();
    }

    @GET
    @Path("/{categoryId}/products")
    @UnitOfWork
    public Response getCategoryProducts(@PathParam("categoryId") String categoryId) {

        List<ProductRepresentation> products = this.categoryService.getCategoryById(categoryId).products
                .stream()
                .map(ProductRepresentation::createFromProduct)
                .collect(Collectors.toList());

        return Response.ok().entity(products).build();
    }

    @POST
    @UnitOfWork
    @RolesAllowed(AccountRole.ADMIN)
    public Response store(@NotNull @Valid CategoryRegistration categoryRegistration) {
        try {

            Category category = this.categoryService.createCategory(
                    Category.createFromRegistration(categoryRegistration)
            );

            return Response.ok().entity(
                    CategoryRepresentation.createFromCategory(category)
            ).build();

        } catch (ConstraintViolationException exception) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT
    @UnitOfWork
    @RolesAllowed(AccountRole.ADMIN)
    public Response put(@NotNull @Valid CategoryRepresentation categoryRepresentation) {
        Category category = this.categoryService.getCategoryById(categoryRepresentation.id);
        category.updateFromRepresentation(categoryRepresentation);

        return Response.ok().entity(
                CategoryRepresentation.createFromCategory(
                        this.categoryService.saveCategory(category)
                )
        ).build();
    }

    @DELETE
    @Path("/{categoryId}")
    @UnitOfWork
    @RolesAllowed(AccountRole.ADMIN)
    public Response delete(@PathParam("categoryId") String categoryId) {
        this.categoryService.deleteCategory(categoryId);

        return Response.ok().build();
    }
}
