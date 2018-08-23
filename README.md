Spring Boot Blogen with Vue.js
===================================
This is a [Vue.js](https://vuejs.org) version of my [Blogen](https://github.com/strohs/springboot-blogen) website.

Blogen is a fictional micro-blogging website that I use to try out various Spring Framework functionality as well as
 various other backend/frontend technologies. The [original](https://github.com/strohs/springboot-blogen) version 
 of Blogen uses Spring Boot along with [thymeleaf](https://www.thymeleaf.org/) as the server-side templating 
 engine. This new version swaps out thymeleaf in favor of Vue.js, turning the website into a single 
 page web application. 
 Additionally, [JSON Web Tokens](https://jwt.io/introduction/) (JWT) are used to provide authorization to logged in 
 users and grant them the use of the Blogen REST API. 

The website functionality remains unchanged and you can read about it on the 
[original](https://github.com/strohs/springboot-blogen) project page. In a nutshell, Blogen is an imaginary community
of users that start threads of discussion on a variety of subjects (called categories). This version of Blogen
 allows you to sign-up as a new user, or you can use a pre-existing user (the pre-existing user names and passwords 
  are available on the login page by clicking the question mark button). 
  Once logged in, you can then Edit, or Delete the threads you start, or reply to other User's threads. 
  Blogen is essentially a basic message board using Vue.js and styled with Bootstrap. Additionally, you can 
  change your user profile settings via the profile link on the top navbar. If you log in as the admin user, 
  you can edit or delete any user's threads (not just your own) as well as edit/create new categories for users.  

Special thanks to [jonashackt](https://github.com/jonashackt/spring-boot-vuejs) for showing how to integrate Vue.js
with Spring Boot.

### Installation and Running
* mvn clean install
* mvn --projects backend spring-boot:run
* Application reachable on [localhost:8080](http://localhost:8080/)

### Project Structure

### JSON Web Token information

### REST API additions
* Swagger UI is [here](http://localhost:8080/swagger-ui.html#/) once you start the project
* Swagger API docs are [here](http://localhost:8080/v2/api-docs) once you start the project

### DynamoDB Branch

### Spring Profiles

#### Technologies used
* On the Frontend
    * Vue.js 2.5 (using npm and webpack as the build tools for the frontend)
        * Vuex
        * Vue Router
        * axios
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