Notes on Spring Securities Oauth2 classes
=============================================
* `OAuth2AuthorizationRequestRedirectWebFilter`
    *  (possibly for reactive? app)
* `Oauth2AuthorizationCodeGrantWebFilter`
    * process Auth. Resp. (for reactive?)
* `Oauth2AuthorizationCodeGrantFilter`
    * processes Auth. Resp. (for regular servlet env.?)
* `OAuth2AuthorizationRequestRedirectFilter`
    * initiates auth code grant or implicit flow, redirects end-user's user agent to Auth. Server's authorization endpoint
    * By default, responds to authorization requests at the URI `/oauth2/authorization/{registrationId}`
* `OAuth2LoginAuthenticationFilter`
    * the impl. of AbstractAuthenticationProcessingFilter for Oauth2 login
    * handles processing of an Auth. Resp. for auth. code grant flow
    * delegates an `Oauth2LoginAuthenticationToken` to the `AuthenticationManager`
    * the AuthenticationManager will call `OAuth2LoginAuthenticationProvider` to exchange auth code for auth token 
    and then create `OAuth2LoginAuthenticationToken`
    * builds an `OAuth2AuthenticationToken` from the details of `OAuth2LoginAuthenticationToken`
    * builds an `OAuth2AuthorizedClient`
    * finally returns the `OAuth2AuthenticationToken`
    

* `OAuth2LoginAuthenticationToken` impl. Authentication
    * An AbstractAuthenticationToken for OAuth 2.0 Login, which leverages the OAuth 2.0 Authorization Code Grant Flow
* `OAuth2AuthenticationToken` impl. Authentication
    * An implementation of an AbstractAuthenticationToken that represents an OAuth 2.0 Authentication.
    * The `Authentication` associates an `OAuth2User` Principal to the identifier of the Authorized Client, which the 
    End-User (Principal) granted authorization to so that it can access it's protected resources at the UserInfo Endpoint.
* `OAuth2AccessTokenResponse`

* `OAuth2LoginAuthenticationProvider`
    * "exchanges" an authorization grant credential (e.g. an Authorization Code) for an access token credential at 
    the Authorization Server's Token Endpoint. (uses a `OAuth2AccessTokenResponseClient`)
    * builds a`OAuth2AccessToken` to retrieve user info. from the auth server (or resource server)
    * builds a `OAuth2User impl. by DefaultOAuth2User` and gives them 'ROLE_USER'
    * finally returns an `OAuth2LoginAuthenticationToken` containing all the details