package com.tdd.user.gateways.controllers;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.config.AbstractIntegrationTest;
import com.tdd.user.domains.SerasaStatus;
import com.tdd.user.domains.SerasaStatusWrapper;
import com.tdd.user.domains.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserControllerTest extends AbstractIntegrationTest {

    static final String API_END_POINT = "/users";

    static final String SERASA_STATUS_END_POINT = "/users/serasa/status/";

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private UserController controller;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeClass
    public static void setUpClass() {
        FixtureFactoryLoader.loadTemplates("com.tdd");
    }

    @Before
    public void setUp() {
        mongoTemplate.dropCollection("recommendations");
        mockMvc = standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void doPostSuccess() throws Exception {
        // GIVEN an empty database

        // AND an valid user
        User user = Fixture.from(User.class).gimme("valid");
        String json = objectMapper.writeValueAsString(user);

        // WHEN do post to create user
        MvcResult result = mockMvc.perform(post(API_END_POINT).contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();
        String body = result.getResponse().getContentAsString();
        User resultBody = objectMapper.readValue(body, User.class);

        // THEN should return created
        assertEquals(201, result.getResponse().getStatus());
        assertEquals(true, resultBody.getId() != null);
        assertEquals(true, resultBody.getName().equals("Jack"));
        assertEquals(true, resultBody.getDocument().equals("123456789"));
    }

    @Test
    public void doPostAndGetValidation() throws Exception {
        // GIVEN an empty database

        // AND an valid user
        User user = new User(null, "", "", null);
        String json = objectMapper.writeValueAsString(user);

        // WHEN do post to create user
        MvcResult result = mockMvc.perform(post(API_END_POINT).contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();
        String body = result.getResponse().getContentAsString();
        User resultBody = objectMapper.readValue(body, User.class);

        // THEN should return errors
        assertEquals(422, result.getResponse().getStatus());
        assertTrue(resultBody.getId() == null);
        assertTrue(resultBody.getDocument() == "");
        assertTrue(resultBody.getName() == "");
        assertTrue(resultBody.getErrors() != null);
        assertTrue(resultBody.getErrors().size() == 2);

        assertTrue(resultBody.getErrors().stream().anyMatch(error ->
                error.getMessage().equals("name is mandatory")
        ));
        assertTrue(resultBody.getErrors().stream().anyMatch(error ->
                error.getMessage().equals("document is mandatory")
        ));
    }

    @Test
    public void doGetSerasaStatusSuccess() throws Exception {
        // GIVEN an user already present in the system database

        doPostSuccess();

        // WHEN the get serasa status is called
        MvcResult result = mockMvc.perform(get(SERASA_STATUS_END_POINT + "123456789")).andReturn();
        String body = result.getResponse().getContentAsString();
        SerasaStatusWrapper resultBody = objectMapper.readValue(body, SerasaStatusWrapper.class);

        // THEN should return
        assertEquals(200, result.getResponse().getStatus());
        assertTrue(resultBody.getDocument() == "123456789");
        assertTrue(resultBody.getStatus() == SerasaStatus.PENDING_DEBIT);
        assertTrue(resultBody.getErrors() == null);
    }
}
