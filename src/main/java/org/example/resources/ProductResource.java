package org.example.resources;

import io.dropwizard.hibernate.UnitOfWork;
import org.example.api.product.ProductRegistration;
import org.example.api.product.ProductRepresentation;
import org.example.core.Category;
import org.example.core.Product;
import org.example.enums.AccountRole;
import org.example.service.CategoryService;
import org.example.service.ProductService;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(AccountRole.ADMIN)
public class ProductResource {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductResource(
            ProductService productService,
            CategoryService categoryService
    ) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GET
    @UnitOfWork
    @PermitAll
    public Response getAll() {
        return Response.ok().entity(
                this.productService.getAllProducts().stream()
                        .map(ProductRepresentation::createFromProduct)
                        .collect(Collectors.toList())
        ).build();
    }

    @GET
    @Path("/{productId}")
    @UnitOfWork
    @PermitAll
    public Response get(@PathParam("productId") String productId) {
        return Response.ok().entity(
                ProductRepresentation.createFromProduct(
                        this.productService.getProductById(productId)
                )
        ).build();
    }

    @POST
    @UnitOfWork
    public Response store(@NotNull @Valid ProductRegistration productRegistration) {

        try {

            Product product = Product.createFromRegistration(productRegistration);
            product.category = this.categoryService.getCategoryById(productRegistration.category.id);

            product = this.productService.createProduct(product);

            return Response.ok().entity(product).build();

        } catch (ConstraintViolationException exception) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @PUT
    @UnitOfWork
    public Response put(@NotNull @Valid ProductRepresentation productRepresentation) {

        try {

            Product product = this.productService.getProductById(productRepresentation.id);
            Category category = this.categoryService.getCategoryById(productRepresentation.category.id);

            product.updateFromRepresentation(productRepresentation);
            product.category = category;

            return Response.ok(
                    ProductRepresentation.createFromProduct(
                            this.productService.saveProduct(productRepresentation)
                    )
            ).build();

        } catch (ConstraintViolationException exception) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @DELETE
    @Path("/{productId}")
    @UnitOfWork
    public Response delete(@PathParam("productId") String productId) {
        this.productService.deleteProduct(productId);

        return Response.ok().build();
    }
}
