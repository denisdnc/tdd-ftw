package com.tdd.user.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document
public class User {

    @Id
    private String id;

    private String document;

    private String name;

    private String status;

    private List<Error> errors;
}
