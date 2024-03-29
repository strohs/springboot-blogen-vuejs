package com.blogen.api.v1.controllers;

import com.blogen.api.v1.services.AuthorizationService;
import com.blogen.api.v1.services.UserService;
import com.blogen.api.v1.services.oauth2.OAuth2UserLoginService;
import com.blogen.services.security.WithMockJwt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileNotFoundException;
import java.net.URI;

@WebMvcTest(controllers = {LoginController.class})
public class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorizationService authorizationService;

    @MockBean
    OAuth2UserLoginService oAuth2UserLoginService;

    @MockBean
    UserService userService;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void readIndexHtmlFromClassPath() throws FileNotFoundException {
        ClassPathResource resource = new ClassPathResource("public/index.html");
        assert (resource.exists());
    }

    @Test
    public void uriComponentBuilderTest() {
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path("/login")
                .queryParam("token", "aaaa.1111122222.bgggrfgfg333")
                .queryParam("login").build().encode();

        final URI uri = uriComponents.toUri();
        System.out.println(uri.toString());
        //assertEquals("/junit-5", uriComponents.toUriString());
    }
}