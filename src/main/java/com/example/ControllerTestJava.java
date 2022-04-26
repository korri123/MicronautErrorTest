package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/api/v1")
public class ControllerTestJava {
    @Get("/getJava")
    public Model getModel() {
        return new Model("value1", "value2");
    }
}
