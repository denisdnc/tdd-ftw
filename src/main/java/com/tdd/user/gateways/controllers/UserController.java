package com.tdd.user.gateways.controllers;

import com.tdd.user.domains.User;
import com.tdd.user.usecases.CreateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private CreateUser createUser;

    @Autowired
    public UserController(CreateUser createUser) {
        this.createUser = createUser;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User create(@RequestBody User user) {
        return createUser.execute(user);
    }

}
