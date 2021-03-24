package org.example;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.example.core.*;
import org.example.db.AccountDAO;
import org.example.db.CategoryDAO;
import org.example.db.OrderDAO;
import org.example.db.ProductDAO;
import org.example.resources.*;
import org.example.auth.JWTAuthenticator;
import org.example.auth.JWTAuthorizer;
import org.example.service.AccountService;
import org.example.service.CategoryService;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class DropwizardApplicationApplication extends Application<DropwizardApplicationConfiguration> {

    private final HibernateBundle<DropwizardApplicationConfiguration> hibernate = new HibernateBundle<DropwizardApplicationConfiguration>(
            Account.class, Product.class, Category.class, Order.class, OrderItem.class
    ) {

        @Override
        public DataSourceFactory getDataSourceFactory(DropwizardApplicationConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new DropwizardApplicationApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropwizardApplication";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardApplicationConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);

    }

    @Override
    public void run(final DropwizardApplicationConfiguration configuration,
                    final Environment environment) {

        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "*");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter("Access-Control-Allow-Origin", "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // DAO layer
        AccountDAO accountDAO = new AccountDAO(hibernate.getSessionFactory());
        ProductDAO productDAO = new ProductDAO(hibernate.getSessionFactory());
        CategoryDAO categoryDAO = new CategoryDAO(hibernate.getSessionFactory());
        OrderDAO orderDAO = new OrderDAO(hibernate.getSessionFactory());

        // services layer
        AccountService accountService = new AccountService(accountDAO);
        CategoryService categoryService = new CategoryService(categoryDAO);
        ProductService productService = new ProductService(productDAO, categoryService);
        OrderService orderService = new OrderService(orderDAO);

        // resource layer
        environment.jersey().register(new AccountResource(accountService, orderService));
        environment.jersey().register(new AuthenticationResource(accountDAO));
        environment.jersey().register(new ProductResource(productService, categoryService));
        environment.jersey().register(new CategoryResource(categoryService));
        environment.jersey().register(new OrderResource(orderService, productService));

        JWTAuthenticator jwtAuthenticator = new UnitOfWorkAwareProxyFactory(hibernate)
                .create(JWTAuthenticator.class, AccountDAO.class, accountDAO);

        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<Account>()
                        .setAuthenticator(jwtAuthenticator)
                        .setAuthorizer(new JWTAuthorizer())
                        .setPrefix("Bearer")
                        .buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Account.class));
    }

}
