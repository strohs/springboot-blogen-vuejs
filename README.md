Spring Boot Blogen with Vue.js
===================================
This is a [Vue.js](https://vuejs.org) version of my [Blogen](https://github.com/strohs/springboot-blogen) fullstack application.

Blogen is a fictitious micro-blogging / message board website that I use as a basis for exploring different frontend and backend
technologies. 
The [first](https://github.com/strohs/springboot-blogen) version of Blogen uses Spring Boot and Spring MVC 
to power the backend, along with [thymeleaf](https://www.thymeleaf.org/) to render the view layer.

This version of Blogen has swapped out thymeleaf for Vue.js and Bootstrap 4.


In addition, the following functionality has been added:
- JSON Web Token (JWT) support has been integrated in order to secure the REST Api endpoints using Spring Security 5.
JWTs are issued to clients after successfully logging into Blogen via a username/password form, and then sent by
the vue.js client during each request to the frontend.
- OAuth2 support has been added, giving users the option to login using their GitHub or Google credentials. This
functionality will require you to register this spring boot application as an OAuth2 client with GitHub and or Google.
This is a free service provided by them. Information on how to do this is [below](#oauth2-configuration).

To keep things somewhat simple, the Spring Boot backend is performing multiple roles:
- serves as the REST Api Server
- serves the html,css and javascript resources needed by the Vue.js frontend
- acts as an OAuth2 client *and* resource server, making requests to GitHub or Google, to log in users to Blogen

In a real production environment, these roles would ideally be handled be separate servers.

No changes have been made to the base, Blogen, website functionality. You can read about that on the 
[original](https://github.com/strohs/springboot-blogen) project page.


## Installation and Running
### Prerequisites
* at least Java 11
* Apache maven (at least version 3+) *OR* you can use the included maven wrapper `mvnw` 
(located in the project's root directory):
    * `mvnw.cmd` if you are running on Windows
    * `mvnw` if you are running on a *nix operating system 
* npm version 14+, is needed only if you plan to develop on the frontend. Note that maven is currently configured to
download node and npm automatically, via the "maven-frontend-plugin". This feature was added 
to support users that may not already have node.js/npm installed. The downloaded node.js files will be placed in
the vue-frontend/node directory.

### Build Steps 
1. make sure you are in the project's root directory: `springboot-blogen-vuejs`
2. Clean and compile the application:
   -`mvn clean install`
        - if you don't already have maven installed, you may use the maven wrapper provided in the root directory of this project: 
            - on linux/mac-os systems replace `mvn` with: `./mvnw clean install`
            - on Windows systems replace `mvn` with: `./mvnw.cmd clean install`
        - this will compile the backend, run tests, AND download node.js/npm in order to build the vue.js resources.
          The download is only performed the first time you run mvn install
3. Start the spring boot application:
    * `mvn --projects backend spring-boot:run`
4. Open your web browser and navigate to [localhost:8080](http://localhost:8080/)
5. Login to the blogen website (login button is located in the upper right corner of the webpage). You can login using an existing
dummy user such as "mcgill" with password: "password", or you could register yourself as a new user.
6. If you want to use GitHub or Google to log in via Oauth2, you will need to first register your (locally running) Blogen
project as a client application. Google/GitHub will generate a `client-id` and `client-secret` string that you must
configure within `application.properties`, and then restart spring boot. 
See the OAuth2 Configuration section (below) for the configuration steps.


### OAuth2 Configuration
You will need to configure one (or both) of these if you want to use your own GitHub or Google userId and password to login to the Blogen website.

#### GitHub OAuth2 Client Creation Instructions
In order to use spring boot as an OAuth2 client with GitHub, you must register it with GitHub. This will enable you to
use your GitHub username and password to log in to the Blogen website running locally on your machine.
Instructions to create a GitHub OAuth2 Client application are [here](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)

When configuring the Oath2 Client (within GitHub's configuration page), be aware of the following:
* the home page url does not matter - I used `http://localhost:8080`
* the `Authorization callback URL` is very important. Spring Security uses a default path for Oauth2 redirects. You
should use the following URL: `http://localhost:8080/login/oauth2/code/github`
* GitHub will generate a `client-id` and `client-secret`. Copy those values into Spring Boot's 
[application.properties](backend/src/main/resources/application.properties) for the following keys:
* `spring.security.oauth2.client.registration.github.client-id`=
* `spring.security.oauth2.client.registration.github.client-secret`=

#### Google Oauth2 Client Creation Instructions
This is similar to the GitHub configuration.
The instructions to create Google Oauth2 application are [here](https://developers.google.com/identity/protocols/OAuth2WebServer)
Follow that guide to create OAuth2 Authorization Credentials, and make sure the Authorized redirect URL is set to:
`http://localhost:8080/oauth2/authorization/google`

Google will generate a `client-id` and `client-secret` string. Copy them into 
[application.properties](backend/src/main/resources/application.properties) using the following keys:
* `spring.security.oauth2.client.registration.google.client-id`=
* `spring.security.oauth2.client.registration.google.client-secret`=


## Project Structure
This is a multimodule maven project with the vue.js code located in the *vue-frontend* directory and the spring boot
code located in the *backend* directory.


## Security
Blogen uses Spring Security 5 JWT support to protect the REST Api endpoints. 
Authenticated users will receive a JWT in two ways:
1. by using Spring's standard "form" login and providing their **Blogen** username and password
2. by using OAuth2 and logging into their GitHub or Google account

Spring Security has been [configured](backend/src/main/java/com/blogen/config/SpringSecConfig.java) to handle the 
above two scenarios.

### OAuth 2.0
[Spring Security 5](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html) 
is used to provide OAuth2 client support. Currently, Spring Security 5 only supports 
the [Authorization Code Grant](https://oauth.net/2/grant-types/authorization-code/) flow to perform a login with 
one of the OAuth2.0 providers you have configured (in your application.properties file). 
However, because Blogen is a single page application (using Vue.js), it will not play nice with all the 
redirects that occur during a typical OAuth2 login process. To work around this, I added the following functionality:

- ignore the default Spring Security generated oauth2 login page, and modify our main vue.js login page to 
trigger the OAuth sign in process using Springs default OAuth2 sign-in endpoint of: `/oauth2/authorization/{providerID}`
- configure a custom "AuthenticationSuccessHandler", located in the 
[LoginController](backend/src/main/java/com/blogen/api/v1/controllers/LoginController.java) that converts user
credentials provided from the OAuth2 provider into a Blogen JWT, and then returns the main 
index.html back to the web browser... along with a cookie containing the JWT
- the vue.js application will re-load the main page and check for the presence of this cookie.
If present, it will call a REST API endpoint to retrieve the user's (complete) details from the database

NOTE: that the above process may not be the "best" approach for handling OAuth2 logins with single page applications. 
I wanted to try and get the authorization code grant flow working with vue.js and Spring Security, rather than using the 
**implicit flow**, which is what a majority of single page application use. Additionally, as of writing this, Spring
Security does not (yet) support the implicit flow.
 
#### OAuth 2.0 Resource Server
Blogen also uses Spring Security's OAuth2 resource server support to secure its api endpoints.
Out of the box, it supports using JSON Web Tokens to provide authentication and authorization.
The resource server is activated using the following Spring Security configurer:

        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new RestApiAuthenticationEntryPoint())
                        .accessDeniedHandler(new RestApiAccessDeniedHandler()) 

Spring Security resource server will take care of validating any JWT sent into protected endpoints.
To actually secure a REST Api endpoint, use the HttpConfigurer with the following matcher:
`.antMatchers("/api/**").hasAuthority("SCOPE_ROLE_API")`

Spring Security will then require all requests hitting that endpoint to have a (valid) JWT with a scope of:
`SCOPE_ROLE_API`, or whatever simple granted authority you wish to use.
 


### Blogen JSON Web Tokens
The JWTs generated by Blogen contain a header and payload using the following structure:
```
{
  "alg": "RS256"
}
{
  "iss": "self",
  "sub": "3",
  "exp": 1644510769,
  "iat": 1644508969,
  "scope": "ROLE_USER ROLE_API"
}
```
The example token above is signed using RS256. The JWT payload contains a subject *sub* of 3, which is the user's 
internal database ID within the Blogen `User` table.
The scope field contains the user's security role(s), either 'ROLE_USER' to indicate they are a plain user within
blogen, or 'ROLE_ADMIN' to indicate they are an administrator of the website. Additionally, all users of blogen get
the 'ROLE_API' scope granted to them. This role gives them access to the blogen REST API. 
Lastly, there are the standard JWT claims: "iat" (issued at time) and "exp" (expiration) fields, 
used to indicate when the token was issued and when it expires.

### Authorization flow summary
- A user can log into Blogen with their username and password, OR using their Github or Google account 
- once successfully logged in, Blogen will return a JWT to the vue.js client
- Vue.js will send the JWT with every request to the REST API.
- Note that Spring Boot does not store the JWT in some datastore, it is "session-less"
  - The token is sent within the HTTP Authorization header using the *Bearer* schema. For example
        * `Authorization: Bearer fgnkjewrt3459hhh34rt.df93249odfgksdflhweurl4598gfufgdlhgdf9345...`
    * The OAuth2 resource server, configured in spring security, will check for the presence of this token, validate it, 
    and then use the scopes within the token to set up `SimpleGrantedAuthorities`


## REST API
Once Blogen is up and running on your localhost, the URLs for the OpenAPI (a.k.a swagger) documentations will be located here:
* [Blogen Swagger UI](http://localhost:8080/swagger-ui.html/)
* [Blogen Swagger API docs](http://localhost:8080/v3/api-docs)

Accessing the Rest API via Swagger will require you to provide your authentication token to Swagger. You can find 
your current token by first logging into blogen and then clicking the *Authentication Token* button located under
the "Welcome" dropdown menu in the upper right of the navbar. Once you've copied the token, navigate to the 
swagger-ui page and click the green authenticate button. In the dialog box that appears you must enter your token 
prefixed with "Bearer " (note that there is a space after Bearer), for example:
* `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiaWF0IjoxNTM1MzMzNTQ0LCJleHAiOjE1MzUzMzUzNDR9......`
 

#### Technologies used
* On the Frontend
    * Vue.js 2.5 (using npm and webpack)
        * Vuex
        * Vue Router
        * axios (to make request to the Blogen REST API)
        * Bootstrap 4
        * [Bootstrap Vue](https://bootstrap-vue.js.org/)
* On the Backend
    * Spring Boot 2.6.3
        * Spring Security 5.2.0
            * OAuth2.0 Resource Server
            * OAuth2.0 Login
        * Spring MVC
    * [Project Lombok](https://projectlombok.org/)
    * [Mapstruct](http://mapstruct.org/)
    * H2 embedded, in-memory, database
    * [JSON Web Tokens](https://jwt.io/introduction/)