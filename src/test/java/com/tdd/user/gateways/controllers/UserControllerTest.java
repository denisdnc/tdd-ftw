package com.tdd.user.gateways.controllers;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.config.AbstractComponentTest;
import com.tdd.user.domains.SerasaWrapper;
import com.tdd.user.domains.User;
import com.tdd.user.gateways.database.UserGateway;
import com.tdd.user.gateways.httpclient.SerasaGateway;
import com.tdd.user.gateways.httpclient.SerasaHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
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


public class UserControllerTest extends AbstractComponentTest {

    private static final String API_END_POINT = "/users";

    @Mock
    private SerasaHttpClient serasaHttpClient;

    @Autowired
    private UserController controller;

    @Autowired
    @InjectMocks
    private SerasaGateway serasaGateway;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeClass
    public static void setUpClass() {
        FixtureFactoryLoader.loadTemplates("com.tdd");
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //mongoTemplate.dropCollection("users");
        mockMvc = standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }


    @Test
    public void doPostWithSuccess() throws Exception {
        // Dado um usuário válido deve retornar HTTP status 201
        User user = Fixture.from(User.class).gimme("valid");
        String json = objectMapper.writeValueAsString(user);

        // e uma resposta válida do serasa
        Mockito.when(serasaHttpClient.getStatus(Mockito.any()))
                .thenReturn(Fixture.from(SerasaWrapper.class).gimme("pendente"));

        // quando
        MvcResult result = mockMvc.perform(post(API_END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();

        // entao o result deve conter o seguinte estado
        assertEquals("O status deve ser:", HttpStatus.CREATED.value(), result.getResponse().getStatus());

        User bodyResult =
                objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertEquals("O name deve ser:", "Jack", bodyResult.getName());
        assertEquals("O document deve ser:", "123456789", bodyResult.getDocument());
        assertTrue("O id não deve ser nulo: ", StringUtils.isNotBlank(bodyResult.getId()));
        assertTrue("A lista deve ser vazia", bodyResult.getErrors().isEmpty());
        assertEquals("O status deve ser PENDING", "PENDING", bodyResult.getStatus());
    }

    @Test
    public void doPostWithError() throws Exception {
        // Dado um usuário inválido deve retornar HTTP status 422
        User user = Fixture.from(User.class).gimme("invalid");

        String json = objectMapper.writeValueAsString(user);

        // quando
        MvcResult result = mockMvc.perform(post(API_END_POINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();

        User bodyResult =
                objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        // entao o result deve conter o seguinte estado
        assertEquals("O status deve ser:", HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getResponse().getStatus());
        assertTrue("A lista deve conter dois erros", bodyResult.getErrors().size() == 2);
        assertTrue("Deve conter o erro de documento obrigatorio",
                bodyResult.getErrors().stream().anyMatch(error -> "document is mandatory".equals((error.getMessage()))));
        assertTrue("Deve conter o erro de nome obrigatorio",
                bodyResult.getErrors().stream().anyMatch(error -> "name is mandatory".equals((error.getMessage()))));
        assertTrue("O campo document deve ser vazio", StringUtils.isBlank(bodyResult.getDocument()));
        assertTrue("O campo name deve ser vazio", StringUtils.isBlank(bodyResult.getName()));
        assertTrue("O campo id deve ser vazio", StringUtils.isBlank(bodyResult.getId()));
    }
}
