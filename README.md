Spring Boot Blogen with Vue.js
===================================
This is a [Vue.js](https://vuejs.org) version of my [Blogen](https://github.com/strohs/springboot-blogen) website.

Blogen is a fictional micro-blogging website / message board that I use to try out various Spring Framework 
functionality as well as various other backend/frontend technologies. The 
[original](https://github.com/strohs/springboot-blogen) version of Blogen uses Spring Boot along 
with [thymeleaf](https://www.thymeleaf.org/).  This version swaps out thymeleaf in favor of Vue.js, a javascript single
page application framework that handles all of the "view" related functionality. 
Spring boot basically becomes a resource server for the REST Api that the vue.js client uses.

[JSON Web Tokens](https://jwt.io/introduction/) (JWT) support has also been added as the main form of authentication /
authorization credential. They are issued to clients after successfully logging in to Blogen via a username/password 
form or via OAuth 2.0 (either github or google). See the JWT section below for more information on how JWTs are used.

The website functionality remains unchanged and you can read about it on the 
[original](https://github.com/strohs/springboot-blogen) project page. In a nutshell, Blogen is a message
board that allows users to start threads of discussion on different subjects (called categories). This version of 
Blogen allows you to sign-up a new user, perform CRUD operations on threads and posts, as well as perform basic 
 filtering operations on threads and posts. If you log in as an admin user, 
  you can edit or delete any threads (not just your own) as well as edit/create new categories for users.  

Big thanks to [jonashackt](https://github.com/jonashackt/spring-boot-vuejs) for explaining how to integrate Vue.js
with Spring Boot.


### Installation and Running
#### Build Prerequisites
* at least Java 8. I've successfully used Open JDK 11 as well.
* Apache maven installation *OR* you can use the included maven wrapper `mvnw` (in the project's root directory):
    * `mvnw.cmd` if you are running on windows
    * `mvnw` if you are running on a *nix operating system 


#### Build Steps 
1. `cd` into the project's root directory
2. Clean and compile the application:
    * `mvn clean install`
        * if you don't have maven installed use maven wrapper: 
            * on *nix systems replace `mvn` with: `./mvnw clean install`
            * on windows systems replace `mvn` with: `./mvnw.cmd clean install` 
3. Start the spring boot application:
    * `mvn --projects backend spring-boot:run`
3. Open your web browser and navigate to [localhost:8080](http://localhost:8080/)
4. If you want to use Github or Google to login via Oauth2, you will need to register your own client application
 with Google and/or Github. They will generate a client-id and client-secret that you must copy into application.properties
 ... see below for more info
 
#### Github OAuth2 Client Creation Instructions
* instructions to create an OAuth2 Client application are [here](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)
* home page url does not matter - i used http://localhost:8080
* `Authorization callback URL` is very important as Spring has a default path it uses for Oauth2 redirects
    * `http://localhost:8080/login/oauth2/code/github`
* Github will generate a client-id and client-secret, copy those into application.properties for Github:
    * spring.security.oauth2.client.registration.github.client-id=
    * spring.security.oauth2.client.registration.github.client-secret=

#### Google Oauth2 Client Creation Instructions
* instructions to create Google Oauth2 application are [here](https://developers.google.com/identity/protocols/OAuth2WebServer)
* follow that guide to create OAuth2 Authorization Credentials, make sure the Authorized redirect URL is set to:
    * `http://localhost:8080/oauth2/authorization/google`
* Google will generate a client-id and client-secret properties, copy them to application.properties
    * spring.security.oauth2.client.registration.google.client-id=
    * spring.security.oauth2.client.registration.google.client-secret=


### Project Structure
This is a multi-module maven project with the vue.js code located in the *vue-frontend* module and the spring boot code
located in the *backend* module.


## Security
As mentioned previously, Blogen uses JWTs as the primary means of authentication and authorization. Users receive
a JWT in two ways:
1. by using Spring's standard "form" login and providing their **Blogen** username and password
2. by using OAuth2 and logging into their Github or Google account

Spring Security has been [configured](backend/src/main/java/com/blogen/config/SpringSecConfig.java) to handle the 
above scenarios, and it has been configured as an OAuth2 "login" and Resource Server.

### OAuth 2.0
Spring is currently in the process of rolling in functionality from their previous 
[OAuth2 project](https://spring.io/projects/spring-security-oauth) into the main Spring Security module.
(more info [here](https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Features-Matrix)). Therefore, 
Blogen is using the support for OAuth 2.0 in Spring Security (version 5.2.0), rather than the  
Spring-Security-Oauth project.

#### OAuth 2.0 Login
OAuth2.0 Login has been configured using the `oauth2Login()` configurer. By default, is uses the
 [Authorization Code Grant](https://oauth.net/2/grant-types/authorization-code/) flow to perform a login with 
 one of the OAuth2.0 providers you have configured (in your application.properties file). 
 However, because Blogen is a single page application, it will not play nice with all the 
 redirects that occur during a typical OAuth2 login process. To workaround this, I added the following functionality: 
* ignore the default Spring Security generated oauth2 login page, and modify our main vue.js login page to 
trigger the OAuth sign in process using Springs default sign-in endpoint of: `/oauth2/authorization/{providerID}`
* configure a custom `AuthenticationSuccessHandler` (located in the LoginController) that takes the final 
authenticated user information, converts it into a Blogen JWT, and then returns the main index.html back to the 
 web browser along with a cookie that contains the JWT
* the vue.js application will re-load the main page and check for the presence of this cookie. If present, it will
call a REST API function to validate the JWT and if it is valid, vue.js will consider the user successfully logged in
and proceed to the main posts page

NOTE that the above process may not be the "best" approach for handling OAuth2 logins with single page applications. 
I really wanted to try and get the authorization code grant flow working with vue.js and Spring, rather than using the 
**implicit flow**, which is what a majority of single page application use. The implicit flow doesn't require
 browser redirects but it less secure than authorization code grant.
 
#### OAuth 2.0 Resource Server
Blogen also uses Spring Security's resource server support to secure api endpoints. Out of the box, it supports 
using JSON Web Tokens to provide authentication and authorization. The resource server is activated using
the following configurer:

        .oauth2ResourceServer()
            .authenticationEntryPoint(unauthorizedHandler)
                .jwt()
                    .decoder(jwtDecoder()) 

To secure an endpoints, we use the following ant matcher that indicates the scope a user must have to access the endpoint:
`.antMatchers("/api/**").hasAuthority("SCOPE_API")`

The resource server expects the JWT to be sent with each request as a "Bearer" token in the Authorization header. 
It will then validate the token (using our own RSA Public Key configured in application.properties), then check that 
the JWT has not expired, then convert all the scopes into SimpleGrantedAuthorities (prefixing each authority with the
String "SCOPE_".    
See the docs at [OAuth2.0 Resource Server](https://docs.spring.io/spring-security/site/docs/5.2.0.BUILD-SNAPSHOT/reference/htmlsingle/#oauth2resourceserver)  


#### Blogen JSON Web Tokens
The JWT generated by blogen contains a header and payload similar to the following:

    {
      "typ": "JWT",
      "alg": "RS256"
    }
    {
       "sub": "6",
       "scope": [
         "USER",
         "API"
       ],
       "exp": 1552947450,
       "iat": 1552945650
     }

The example token above is signed using RSA256. The payload contains a subject *sub* of 6, which is the user's 
internal ID within the Blogen User table. The scope field contains the user's role (either 'USER' or 'ADMIN') and a custom
scope specific to Blogen named 'API', which gives a user permission to use the rest API. Lastly, there are the 
"iat" (issued at time) and "exp" (expiration) fields, used to indicate when the token was issued and when it expires.

#### Authorization flow summary
* A user can log into Blogen with their username and password, OR using their Github or Google account 
* once successfully logged in, Blogen will return a JWT to the vue.js client
* Vue.js will send the JWT (via axios) with every HTTP request to spring boot so that it can access the website 
and REST api. 
* Spring Boot does not store the token in some datastore
    * The token is sent within the HTTP Authorization header using the *Bearer* schema. For example
        * `Authorization: Bearer fgnkjewrt3459hhh34rt.df93249odfgksdflhweurl4598gfufgdlhgdf9345...`
    * The OAuth2 resource server provided by spring security will check for the presence of this token, validate it, 
    and then use the scopes within the token to set-up Granted Authorities


### REST API
Once Blogen is up and running, the URLs for the swagger api documentations are here:
* Swagger UI is [here](http://localhost:8080/swagger-ui.html#/)
* Swagger API docs are [here](http://localhost:8080/v2/api-docs)

Accessing the Rest API via Swagger will require you to provide your authentication token to Swagger. You can find 
your current token by first logging into blogen and then clicking the *Authentication Token* button located under t
he "Welcome" dropdown menu in the upper right of the navbar. Once you've copied the token, navigate to the 
swagger-ui page and click the green authenticate button. In the dialog box that appears you must enter your token 
prefixed with "Bearer " (note that there is a space after Bearer), for example:
* `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiaWF0IjoxNTM1MzMzNTQ0LCJleHAiOjE1MzUzMzUzNDR9......`
 

#### Technologies used
* On the Frontend
    * Vue.js 2.5 (using npm and webpack as the build tools for the frontend)
        * Vuex
        * Vue Router
        * axios (to make request to the REST API)
        * Bootstrap 4
        * [Bootstrap Vue](https://bootstrap-vue.js.org/)
* On the Backend
    * Spring Boot 2.1.3
        * Spring Security 5.2.0
            * OAuth2.0 Resource Server
            * OAuth2.0 Login
        * Spring MVC
    * [Project Lombok](https://projectlombok.org/)
    * [Mapstruct](http://mapstruct.org/)
    * H2 embedded database
    * [JSON Web Tokens](https://jwt.io/introduction/)