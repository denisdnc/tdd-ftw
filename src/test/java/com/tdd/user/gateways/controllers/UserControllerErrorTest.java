package com.tdd.user.gateways.controllers;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.config.AbstractComponentTest;
import com.tdd.user.domains.User;
import com.tdd.user.usecases.CreateUser;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


public class UserControllerErrorTest extends AbstractComponentTest {

    private static final String API_END_POINT = "/users";

    @InjectMocks
    @Autowired
    private UserController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CreateUser createUser;

    @BeforeClass
    public static void setUpClass() {
        FixtureFactoryLoader.loadTemplates("com.tdd");
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }


    @Test
    public void doPostWithServerError() throws Exception{
        // Dado um usuário válido deve retornar HTTP status 201
        User user = Fixture.from(User.class).gimme("valid");

        String json = objectMapper.writeValueAsString(user);

        //deve ser lancado uma excessao
        Mockito.when(createUser.execute(Mockito.any())).thenThrow(new RuntimeException());

        // quando
        MvcResult result = mockMvc.perform(post(API_END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();


        // e o result deve conter o seguinte estado
        assertEquals("O status deve ser:", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                result.getResponse().getStatus());
    }
}
