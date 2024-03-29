package com.blogen.bootstrap;

import com.blogen.domain.*;
import com.blogen.repositories.*;
import com.blogen.api.v1.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Bootstrap the blogen embedded JPA database with data
 *
 * @author Cliff
 */
@Slf4j
//@Component
public class Bootstrapper implements ApplicationListener<ContextRefreshedEvent> {

    private CategoryRepository categoryRepository;
    private UserService userService;
    private PostRepository postRepository;
    private RoleRepository roleRepository;
    private AvatarRepository avatarRepository;

    private static final String IMG_SERVICE = "https://picsum.photos/300/200";
    private static final String IMG_SERVICE_BUSINESS = "https://picsum.photos/300/200/?image=1070";
    private static final String IMG_SERVICE_CITY = "https://picsum.photos/300/200/?image=1071";
    private static final String IMG_SERVICE_ABSTRACT = "https://picsum.photos/300/200/?image=1072";
    private static final String IMG_SERVICE_NATURE = "https://picsum.photos/300/200/?image=1073";
    private static final String IMG_SERVICE_FOOD = "https://picsum.photos/300/200/?image=1074";
    private static final String IMG_SERVICE_GREY = "https://picsum.photos/g//300/200/?image=1075";

    @Autowired
    public Bootstrapper(CategoryRepository categoryRepository,
                        PostRepository postRepository,
                        RoleRepository roleRepository,
                        AvatarRepository avatarRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.avatarRepository = avatarRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void initData() {
        //BUILD ROLES
        Role userRole = new Role();
        userRole.setRole("ROLE_USER");
        roleRepository.save(userRole);
        log.info("Saved role" + userRole.getRole());
        Role adminRole = new Role();
        adminRole.setRole("ROLE_ADMIN");
        roleRepository.save(adminRole);
        log.info("Saved role" + adminRole.getRole());


        //BUILD CATEGORIES
        Category business = CategoryBuilder.build("Business");
        Category webDev = CategoryBuilder.build("Web Development");
        Category tech = CategoryBuilder.build("Tech Gadgets");
        Category health = CategoryBuilder.build("Health & Fitness");
        categoryRepository.save(business);
        categoryRepository.save(webDev);
        categoryRepository.save(tech);
        categoryRepository.save(health);

        //Build Avatars - (these files should exist in the /static/avatars/ directory
        Avatar av0 = Avatar.builder().fileName("avatar0.jpg").build();
        av0 = avatarRepository.save(av0);
        Avatar av1 = Avatar.builder().fileName("avatar1.jpg").build();
        av1 = avatarRepository.save(av1);
        Avatar av2 = Avatar.builder().fileName("avatar2.jpg").build();
        av2 = avatarRepository.save(av2);
        Avatar av3 = Avatar.builder().fileName("avatar3.jpg").build();
        av3 = avatarRepository.save(av3);
        Avatar av4 = Avatar.builder().fileName("avatar4.jpg").build();
        av4 = avatarRepository.save(av4);
        Avatar av5 = Avatar.builder().fileName("avatar5.jpg").build();
        av5 = avatarRepository.save(av5);
        Avatar av6 = Avatar.builder().fileName("avatar6.jpg").build();
        av6 = avatarRepository.save(av6);
        Avatar av7 = Avatar.builder().fileName("avatar7.jpg").build();
        av7 = avatarRepository.save(av7);

        //BUILD USER PREFS
        UserPrefs upJohn = UserPrefs.builder().avatar(av3).build();
        UserPrefs upAdmin = UserPrefs.builder().avatar(av2).build();
        UserPrefs upMaggie = UserPrefs.builder().avatar(av1).build();
        UserPrefs upWilliam = UserPrefs.builder().avatar(av4).build();
        UserPrefs upElizabeth = UserPrefs.builder().avatar(av5).build();

        //BUILD USERS
        //
        //retrieve the roles we created
        adminRole = roleRepository.findByRole("ROLE_ADMIN");
        userRole = roleRepository.findByRole("ROLE_USER");

        //administrator
        UserBuilder ub = new UserBuilder("admin", "Carl", "Sagan", "admin@blogen.org", "adminpassword", upAdmin);
        User admin = ub.build();
        admin.addRole(adminRole);
        admin.addRole(userRole);
        admin.setUserPrefs(upAdmin);
        userService.saveUser(admin);

        //JohnDoe
        ub = new UserBuilder("johndoe", "John", "Doe", "jdoe@gmail.com", "password", upJohn);
        User john = ub.build();
        john.addRole(userRole);
        john.setUserPrefs(upJohn);
        userService.saveUser(john);

        //Maggie McGill
        ub = new UserBuilder("mgill", "Maggie", "McGill", "gilly@yahoo.com", "password", upMaggie);
        User maggie = ub.build();
        maggie.addRole(userRole);
        maggie.setUserPrefs(upMaggie);
        userService.saveUser(maggie);

        //William Wallace
        ub = new UserBuilder("scotsman", "William", "Wallace", "scotty@hotmail.com", "password", upWilliam);
        User william = ub.build();
        william.addRole(userRole);
        william.setUserPrefs(upWilliam);
        userService.saveUser(william);

        //Elizabeth Reed
        ub = new UserBuilder("lizreed", "Elizabeth", "Reed", "liz@gmail.com", "password", upElizabeth);
        User elizabeth = ub.build();
        elizabeth.addRole(userRole);
        elizabeth.setUserPrefs(upElizabeth);
        userService.saveUser(elizabeth);


        // BUILD POSTS
        //
        //build posts for John - 1 Parent post with 4 child posts
        PostBuilder pb = new PostBuilder(john, tech, sequentialImageUrl(), "Love this tech", "Smart-phones are the greatest invention in the history of mankind", LocalDateTime.of(2017, 1, 1, 10, 11, 12));
        Post parent = pb.build();
        //postRepository.save( parent );
        pb.addChildPost(john, "Love it too", "I wish I could embed the phone into my head", sequentialImageUrl(), LocalDateTime.of(2017, 1, 1, 10, 12, 12));
        pb.addChildPost(maggie, "Not so fast", "Are they even greater than the Internet?", sequentialImageUrl(), LocalDateTime.of(2017, 1, 1, 10, 13, 12));
        pb.addChildPost(william, "Here today gone tomorrow", "They're the greatest for now, but something better will come along", sequentialImageUrl(), LocalDateTime.of(2017, 1, 1, 10, 14, 12));
        pb.addChildPost(william, "No No No", "the greatest invention is velcro :)", sequentialImageUrl(), LocalDateTime.of(2017, 1, 1, 10, 15, 12));
        postRepository.save(parent);

        //
        //build posts for william - 2 parent posts
        pb = new PostBuilder(william, health, sequentialImageUrl(), "Started lifting today", "Trying to burn off these holiday calories. I hear resistence training is better than running", LocalDateTime.of(2017, 2, 1, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(william, business, sequentialImageUrl(), "Bulls are on parade", "They stock markets won't stop running higher. When will the bubble burst?", LocalDateTime.of(2017, 3, 1, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        //
        //build posts for maggie - 3 parent posts with 2 child posts each
        pb = new PostBuilder(maggie, business, sequentialImageUrl(), "Bitcoin or bust", "Forget about gold, I'm all in on bitcoin", LocalDateTime.of(2017, 4, 1, 10, 11, 12));
        parent = pb.build();
        pb.addChildPost(john, "probably buying it", "I'm game too. I just don't know where to but it from", sequentialImageUrl(), LocalDateTime.of(2017, 4, 2, 10, 11, 12));
        pb.addChildPost(maggie, "beware the bubble", "If we've waited this long, I fear it's already too late", sequentialImageUrl(), LocalDateTime.of(2017, 4, 3, 10, 11, 12));
        postRepository.save(parent);

        pb = new PostBuilder(maggie, health, sequentialImageUrl(), "What ever happened to Jazzercise?", "It used to be all the rage, now I can't find a single gym that offers it", LocalDateTime.of(2017, 5, 1, 10, 11, 12));
        parent = pb.build();
        pb.addChildPost(william, "sounds gross", "Jazzercise? Really? What do you do, listen to jazz until you pass out?", sequentialImageUrl(), LocalDateTime.of(2017, 5, 2, 10, 11, 12));
        pb.addChildPost(william, "on second thought...", "I changed my mind. It looks kinda fun. Someone get me leg warmers", sequentialImageUrl(), LocalDateTime.of(2017, 5, 3, 10, 11, 12));
        postRepository.save(parent);

        pb = new PostBuilder(maggie, webDev, sequentialImageUrl(), "Is PHP dead?", "Does anyone have stats on PHP usage in the wild?", LocalDateTime.of(2017, 6, 1, 10, 11, 12));
        parent = pb.build();
        pb.addChildPost(william, "I doubt it", "PHP is everywhere. I'm pretty sure it still powers the internet!", sequentialImageUrl(), LocalDateTime.of(2017, 6, 2, 10, 11, 12));
        pb.addChildPost(admin, "We don't use it here", "...anymore. We switched to Kotlin/React, but a lot of companies are still powered by PHP", sequentialImageUrl(), LocalDateTime.of(2017, 6, 3, 10, 11, 12));
        postRepository.save(parent);

        //
        //Build Posts for elizabeth (10 parent posts for her)
        pb = new PostBuilder(elizabeth, business, sequentialImageUrl(), "Invest now", "Market returns are crazy, there is still time to jump on in", LocalDateTime.of(2017, 1, 1, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, health, sequentialImageUrl(), "Proper Diet trumps all", "No matter what excercise you do, just remember you can never out-train a poor diet", LocalDateTime.of(2017, 1, 2, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, tech, sequentialImageUrl(), "About Alexa", "Does anyone own one of these? Is it any good?", LocalDateTime.of(2017, 1, 3, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, webDev, sequentialImageUrl(), "Bootstrap 4", "Hey you all. Would it be worth my time to learn Bootstrap 4?", LocalDateTime.of(2017, 1, 4, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, business, sequentialImageUrl(), "Buying gold", "I wanna buy some gold. Can someone point me in the right direction", LocalDateTime.of(2017, 1, 5, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, health, sequentialImageUrl(), "HIIT Training", "Forget about running for hours on end. High Intensity Interval Training can give you all the benefits in half the time", LocalDateTime.of(2017, 1, 6, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, tech, sequentialImageUrl(), "Toys that teach Programming", "My nephew is showing an interest in programming. Can anyone recommend something for a ten year old?", LocalDateTime.of(2017, 1, 7, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, webDev, sequentialImageUrl(), "Clojure Script", "You guys need to try this http://clojure.org, It saved me hours of web dev work", LocalDateTime.of(2017, 1, 8, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, tech, sequentialImageUrl(), "Samsung Galaxy 8", "This phone is the greatest. Nice screen, good battery life, and tons of apps!", LocalDateTime.of(2017, 1, 9, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);

        pb = new PostBuilder(elizabeth, webDev, sequentialImageUrl(), "Spring Framework 5", "I hear webFlux is all the rage in Spring Framework. Does anyone have first hand experience?", LocalDateTime.of(2017, 1, 10, 10, 11, 12));
        parent = pb.build();
        postRepository.save(parent);


        //there should now be 26 posts in total - sixteen parent posts and ten child posts
    }


    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private static int imageStart = 1058;

    ///quick and dirty random image url
    private static String sequentialImageUrl() {
        return IMG_SERVICE + "/?image=" + imageStart++;
    }


    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // NOTE using script based creation for data instead of this bootstrap class, but leaving
        //   class here in case it is needed in the future

        //initData();
        //log.info("Finished bootstrapping data");
    }
}
