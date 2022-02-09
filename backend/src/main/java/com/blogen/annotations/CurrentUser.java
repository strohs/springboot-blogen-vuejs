package com.blogen.annotations;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * CurrentUser annotation is a wrapper around @AuthenticationPrincipal annotation so that we don’t get
 * too much tied up with Spring Security related annotations everywhere in our project.
 * This reduces the dependency on Spring Security.
 *
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
