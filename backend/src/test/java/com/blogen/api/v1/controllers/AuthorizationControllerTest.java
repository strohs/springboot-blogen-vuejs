package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.services.AuthorizationService;
import com.blogen.api.v1.services.PostService;
import com.blogen.api.v1.validators.UserDtoSignupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.blogen.api.v1.controllers.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Authorization Controller Test
 * <p>
 * Author: Cliff
 */
//TODO turn security on and test WebTokens
@WebMvcTest(controllers = {AuthorizationController.class})
@Import({UserDtoSignupValidator.class})
public class AuthorizationControllerTest {

    @MockBean
    AuthorizationService authorizationService;

    @MockBean
    PostService postService;

    @Autowired
    WebApplicationContext webCtx;

    @Autowired
    MockMvc mockMvc;

    private static final String SIGNUP_URL = AuthorizationController.BASE_URL + "/signup";

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void should_returnHttpCreated_when_validUserDTO() throws Exception {
        UserDTO validUserDTO = buildValidUserDTO();
        UserDTO savedUserDTO = buildValidReturnUserDTO(1L);

        given(authorizationService.signUpUser(any(UserDTO.class))).willReturn(savedUserDTO);

        mockMvc.perform(post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(validUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userUrl", is(savedUserDTO.getUserUrl())));
    }

    @Test
    public void should_returnHttpUnprocessableEntity_when_missingEmail() throws Exception {
        UserDTO inUserDTO = buildValidUserDTO();
        inUserDTO.setEmail(""); //blank out the email

        given(authorizationService.signUpUser(any(UserDTO.class))).willReturn(inUserDTO);

        mockMvc.perform(post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(inUserDTO)))
                .andExpect(status().isUnprocessableEntity());
    }


    //build a userDTO with all required fields
    private UserDTO buildValidUserDTO() {
        return UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("jdoe@gmail.com")
                .userName("johnny")
                .password("secretpass")
                .build();
    }

    //build a valid UserDTO with the userUrl set
    private UserDTO buildValidReturnUserDTO(Long id) {
        return UserDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .email("jdoe@gmail.com")
                .userName("johnny")
                .password("secretpass")
                .userUrl(UserController.BASE_URL + "/" + id)
                .build();
    }
}