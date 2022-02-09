package com.blogen.services.security;

/**
 * These are the spring authorities that blogen recognizes for use as "SCOPES" within
 * the JWT(s) that it issues:
 *
 * ROLE_API - end users with those role are allowed to use the Blogen Rest API
 * ROLE_USER - all users that register with the Blogen website get this role
 * ROLE_ADMIN - blogen website administrators get this role
 */
public enum BlogenAuthority {
    ROLE_API,
    ROLE_USER,
    ROLE_ADMIN
}
