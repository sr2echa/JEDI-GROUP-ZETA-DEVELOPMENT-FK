package com.flipfit;

import com.flipfit.resources.*;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * FlipFit DropWizard Application
 * Main entry point for the REST API server
 */
public class FlipFitApplication extends Application<FlipFitConfiguration> {

    public static void main(String[] args) throws Exception {
        new FlipFitApplication().run(args);
    }

    @Override
    public String getName() {
        return "FlipFit";
    }

    @Override
    public void initialize(Bootstrap<FlipFitConfiguration> bootstrap) {
        // Add initialization code here
    }

    @Override
    public void run(FlipFitConfiguration configuration, Environment environment) {
        // Enable CORS
        configureCors(environment);

        // Register resources (REST endpoints)
        environment.jersey().register(new UserResource());
        environment.jersey().register(new CustomerResource());
        environment.jersey().register(new AdminResource());
        environment.jersey().register(new GymOwnerResource());
        environment.jersey().register(new GymCenterResource());
        environment.jersey().register(new SlotResource());
        environment.jersey().register(new BookingResource());
        environment.jersey().register(new PaymentResource());
        environment.jersey().register(new NotificationResource());

        System.out.println("=".repeat(60));
        System.out.println("FlipFit REST API Server Started");
        System.out.println("Server running on: http://localhost:8080");
        System.out.println("=".repeat(60));
    }

    private void configureCors(Environment environment) {
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
