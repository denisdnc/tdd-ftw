package com.tdd.user.gateways.controllers;

import com.tdd.user.domains.SerasaStatusWrapper;
import com.tdd.user.domains.User;
import com.tdd.user.usecases.CreateUser;
import com.tdd.user.usecases.GetSerasaStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private CreateUser createUser;
    private GetSerasaStatus getSerasaStatus;

    @Autowired
    public UserController(CreateUser createUser, GetSerasaStatus getSerasaStatus) {
        this.createUser = createUser;
        this.getSerasaStatus = getSerasaStatus;
    }

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

    @RequestMapping(value = "/serasa/status/{document}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SerasaStatusWrapper> getSerasaStatus(@PathVariable(name = "document") String document) {

        SerasaStatusWrapper result = getSerasaStatus.execute(document);

        if (CollectionUtils.isNotEmpty(result.getErrors())) {
            return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
