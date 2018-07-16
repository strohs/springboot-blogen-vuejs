package com.blogen.api.v1.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper class containing methods used in Rest Controller Tests
 *
 * @author Cliff
 */
public abstract class AbstractRestControllerTest {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
