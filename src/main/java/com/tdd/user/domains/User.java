package com.tdd.user.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String document;
    private List<String> errors;

}
