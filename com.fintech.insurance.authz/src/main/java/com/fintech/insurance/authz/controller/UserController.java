package com.fintech.insurance.authz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @GetMapping(path = "/current")
    public Principal getUser(Principal principal) {
        return principal;
    }
}
