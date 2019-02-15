Spring Boot Blogen with Vue.js
===================================
This is a [Vue.js](https://vuejs.org) version of my [Blogen](https://github.com/strohs/springboot-blogen) website.

Blogen is a fictional micro-blogging website that I use to try out various Spring Framework functionality as well as
 various other backend/frontend technologies. The [original](https://github.com/strohs/springboot-blogen) version 
 of Blogen uses Spring Boot along with [thymeleaf](https://www.thymeleaf.org/).  This new version swaps out 
 thymeleaf in favor of Vue.js. Vue handles the "view" and spring boot basically becomes a "wrapper" for the
  REST Api that Vue connects to.
 Additionally, [JSON Web Tokens](https://jwt.io/introduction/) (JWT) have been added to the project in order 
 to provide authorization to logged in users and grant them access to the website and the REST API. 

The website functionality remains unchanged and you can read about it on the 
[original](https://github.com/strohs/springboot-blogen) project page. In a nutshell, Blogen is an imaginary community
of users that start threads of discussion on different subjects (called categories). This version of Blogen
 allows you to sign-up a new user, perform CRUD operations on threads and posts that you make, and perform basic 
 filtering operations on those threads and posts. If you log in as the admin user, 
  you can edit or delete any threads (not just your own) as well as edit/create new categories for users.  

Special thanks to [jonashackt](https://github.com/jonashackt/spring-boot-vuejs) for explaining how to integrate Vue.js
with Spring Boot.


### Installation and Running
#### Build Prerequisites
* at least Java 8 (later versions of Java will not build some of the packages used in this project)
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


### Project Structure
This is a multi-module maven project with the vue.js code located in the *frontend* module and the spring boot code
located in the *backend* module.


### JSON Web Token Information
This version of Blogen uses [JSON Web Token](https://jwt.io/introduction/) (JWT) to authorize access to the 
Blogen Website and REST api. I am using the [java-jwt](https://github.com/auth0/java-jwt) library to create 
and verify the JWT with a default expiration of 30 minutes (configurable in application.properties).

#### Authorization flow
* Once a user logs in to Blogen with their username and password, they are provided with a JWT. 
* The JWT must be sent with every HTTP request in order to access the website and REST api. 
The server (Spring Boot) does not store the token.
    * The token is sent within the HTTP Authorization header using the *Bearer* schema. For example
        * `Authorization: Bearer fgnkjewrt3459hhh34rt.df93249odfgksdflhweurl4598gfufgdlhgdf9345...`
* The token payload contains the user's internal database ID as the JWT subject. A Spring web 
filter has been written to look for the JWT and use the userID contained within it to authenticate 
and authorize the user with Spring security. 
(see the [JwtAuthenticationFilter](https://github.com/strohs/springboot-blogen-vuejs/blob/master/backend/src/main/java/com/blogen/services/security/JwtAuthenticationFilter.java)  



### REST API
The URLs for the swagger api documentations are here:
* Swagger UI is [here](http://localhost:8080/swagger-ui.html#/)
* Swagger API docs are [here](http://localhost:8080/v2/api-docs)

To access the Swagger UI or API docs you will need to start the project first. Accessing the Rest API via Swagger
will require you to provide your authentication token to Swagger. You can find your current token by first 
logging into blogen and then clicking the *Authentication Token* button located under the "Welcome" dropdown 
menu in the upper right of the navbar. Once you've copied the token, navigate to the swagger-ui page and click the
green authenticate button. In the dialog box that appears you must enter your token prefixed with "Bearer " (note
that there is a space after Bearer), for example:
* `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiaWF0IjoxNTM1MzMzNTQ0LCJleHAiOjE1MzUzMzUzNDR9......`
  

### DynamoDB Branch

### Spring Profiles
* `dev` - using the 'dev' profile will enable CORS support for the Vue/npm development server. This is only
needed during development.

#### Technologies used
* On the Frontend
    * Vue.js 2.5 (using npm and webpack as the build tools for the frontend)
        * Vuex
        * Vue Router
        * axios (to make request to the REST API)
        * Bootstrap 4
        * [Bootstrap Vue](https://bootstrap-vue.js.org/)
* On the Backend
    * Spring Boot 2.0
        * Spring Security
        * Spring MVC
    * [Project Lombok](https://projectlombok.org/)
    * [Mapstruct](http://mapstruct.org/)
    * H2 embedded database
    * [JSON Web Tokens](https://jwt.io/introduction/)