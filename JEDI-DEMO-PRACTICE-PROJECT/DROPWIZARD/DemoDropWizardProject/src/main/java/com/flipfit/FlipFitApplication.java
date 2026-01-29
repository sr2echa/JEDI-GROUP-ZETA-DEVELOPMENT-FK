package com.flipfit;

import com.flipfit.api.*;
import com.flipfit.config.FlipFitConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FlipFitApplication extends Application<FlipFitConfiguration> {

    public static void main(String[] args) throws Exception {
        new FlipFitApplication().run(args);
    }

    @Override
    public String getName() {
        return "FlipFit REST API";
    }

    @Override
    public void initialize(Bootstrap<FlipFitConfiguration> bootstrap) {
        // Any initialization code
    }

    @Override
    public void run(FlipFitConfiguration configuration, Environment environment) {
        // Register REST resources
        final AuthResource authResource = new AuthResource();
        final AdminResource adminResource = new AdminResource();
        final CustomerResource customerResource = new CustomerResource();
        final GymOwnerResource gymOwnerResource = new GymOwnerResource();
        
        environment.jersey().register(authResource);
        environment.jersey().register(adminResource);
        environment.jersey().register(customerResource);
        environment.jersey().register(gymOwnerResource);
        
        // Enable CORS if needed
        // configureCors(environment);
    }
    
    /*
    private void configureCors(Environment environment) {
        final FilterRegistration.Dynamic cors =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
    */
}
