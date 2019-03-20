package com.blogen.repositories;

import com.blogen.domain.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * These tests all use the H2 schema and data located in src/main/resources
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void should_return_a_page_of_four_threads_in_descending_order_of_creation() {
        Pageable pageable = PageRequest.of(0, 4);

        Page<Post> page = postRepository.findAllByParentNullOrderByCreatedDesc(pageable);

        assertThat( page, is( notNullValue()));
        assertThat( page.getSize(), is(4) );
        List<Post> posts = page.getContent();
        // assert that all the returned posts are parent posts (aka starting "threads")
        assertThat( posts.stream().allMatch(Post::isParentPost), is(true) );
        assertThat( posts.get(0).getCreated().compareTo( posts.get(3).getCreated()), greaterThanOrEqualTo(0) );
    }

    @Test
    public void should_return_4_business_parent_posts_when_findAllByCategory_IdAndParentNull() {
        // given there are 6 posts in the Business category, but only 4 are parent posts (aka thread starting posts)
        int expectedPostCount = 4;
        Long businessId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<Post> page = postRepository.findAllByCategory_IdAndParentNull( businessId, pageable);

        assertThat( page.getNumberOfElements(), is(expectedPostCount) );
        assertThat( page.get().allMatch( p -> p.getCategory().getId().equals(businessId)), is(true) );
    }

    @Test
    public void should_return_page_of_5_posts_for_user_5_when_findAllByUser_IdAndParentNull() {
        // given user ID 5 has created 10 parent posts in the database, but we want a page of 5 posts
        long expectedPostCount = 10;
        long userId = 5L;
        Pageable pageable = PageRequest.of(0, 5);

        Page<Post> page = postRepository.findAllByUser_IdAndParentNull(userId, pageable);

        assertThat( page.getNumberOfElements(), is(5) );
        assertThat( page.getTotalElements(), is(expectedPostCount) );
        // all returned posts should be parent posts (aka thread starters)
        assertThat( page.get().allMatch(Post::isParentPost), is(true));
        // all posts should belong to the requested user
        assertThat( page.get().allMatch( p -> p.getUser().getId().equals(userId)), is(true));
    }

    @Test
    public void findAllByUser_IdAndCategory_IdAndParentNull() {
        // given user ID 5 has created 3 parent posts belonging to category 2 (Health & Fitness)
        int expectedPostCount = 3;
        long userId = 5L;
        long categoryId = 2L;
        Pageable pageable = PageRequest.of(0, 5);

        Page<Post> page = postRepository.findAllByUser_IdAndCategory_IdAndParentNull( userId, categoryId, pageable);

        assertThat( page.getNumberOfElements(), is( expectedPostCount ) );
        // all returned posts should be parent posts (aka thread starters)
        assertThat( page.get().allMatch(Post::isParentPost), is(true));
        // all posts should belong to the requested user
        assertThat( page.get().allMatch( p -> p.getUser().getId().equals(userId)), is(true));
    }

    @Test
    public void findByTextOrTitleIgnoreCaseContaining() {
        // given there are 3 posts in the database containing the word "phone"
        String searchStr = "phone";
        Pageable pageable = PageRequest.of(0,10);

        Page<Post> page = postRepository.findByTextOrTitleIgnoreCaseContaining( searchStr, pageable);

        assertThat( page.getNumberOfElements(), is(3) );

    }

}
