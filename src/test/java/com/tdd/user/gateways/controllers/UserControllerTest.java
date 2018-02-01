
package com.tdd.user.gateways.controllers;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.config.AbstractIntegrationTest;
import com.tdd.user.domains.SerasaResponse;
import com.tdd.user.domains.User;
import com.tdd.user.gateways.httpclient.SerasaGateway;
import com.tdd.user.gateways.httpclient.SerasaHttpClient;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class UserControllerTest extends AbstractIntegrationTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private SerasaHttpClient serasaHttpClient;

    @Autowired
    @InjectMocks
    private SerasaGateway serasaGateway;

    @Autowired
    private UserController controller;

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
    public void doPostSuccess() throws Exception {

        //DADO que um usuário válido
        User user = Fixture.from(User.class).gimme("valid");
        String json = objectMapper.writeValueAsString(user);

        //E a api Serasa retorne
        SerasaResponse serasaResponse = Fixture.from(SerasaResponse.class).gimme("NO_DEBIT");
        Mockito.when(serasaHttpClient.getStatus(Mockito.anyString())).thenReturn(serasaResponse);

        //QUANDO fazemos um post na API
        MvcResult result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andReturn();

        //ENTÃO deve ser retornado um usuário com status correto
        assertEquals("O Status deve ser:", HttpStatus.CREATED.value(), result.getResponse().getStatus());


        //E o json de retorno correto
        User bodyResult =
                objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertEquals("expected name jack", bodyResult.getName(), "Jack");
        assertEquals("expected cpf 123456789", bodyResult.getDocument(), "123456789");
        assertTrue("expected no errors", bodyResult.getErrors().isEmpty());
        assertNotNull("expected user id", bodyResult.getId());
    }
}
