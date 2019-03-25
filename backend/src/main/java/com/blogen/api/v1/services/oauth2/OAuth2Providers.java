package com.blogen.api.v1.services.oauth2;

/**
 * Enum that lists the OAuth2 Providers supported by Blogen
 */
public enum OAuth2Providers {
    GITHUB,
    GOOGLE;

    public OAuth2Providers getProvider(String name) {
        switch (name.toLowerCase()) {
            case "github": return OAuth2Providers.GITHUB;
            case "google": return OAuth2Providers.GOOGLE;
            default: throw new IllegalArgumentException("unknown provider name: " + name);
        }
    }
}
