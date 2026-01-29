package com.flipfit.config;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FlipFitConfiguration extends Configuration {
    
    @JsonProperty
    private String databaseUrl;
    
    @JsonProperty
    private String databaseUser;
    
    @JsonProperty
    private String databasePassword;
    
    public String getDatabaseUrl() {
        return databaseUrl;
    }
    
    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }
    
    public String getDatabaseUser() {
        return databaseUser;
    }
    
    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }
    
    public String getDatabasePassword() {
        return databasePassword;
    }
    
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
