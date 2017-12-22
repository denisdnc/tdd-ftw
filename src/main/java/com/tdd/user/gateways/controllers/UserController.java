package com.tdd.user.gateways.controllers;

import com.tdd.user.domains.Error;
import com.tdd.user.domains.User;
import com.tdd.user.usecases.CreateUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserController(CreateUser createUser) {
        this.createUser = createUser;
    }

    private CreateUser createUser;


    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> create(@RequestBody User user) {
        User result = createUser.execute(user);

        if (CollectionUtils.isNotEmpty(result.getErrors())) {
            return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<User> handleError() {
        return new ResponseEntity<>(new User(null, null, null, null,
                Arrays.asList(new Error("Infernal Server Error"))), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
