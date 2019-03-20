package com.blogen.repositories;

import com.blogen.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring-Data-JPA query methods for the Post table
 *
 * @author Cliff
 */
public interface PostRepository extends JpaRepository<Post,Long> {

    /**
     * find all parent posts. Parent posts have a PARENT_ID = NULL in the database.
     * @return a List containing all parent posts.
     */
   List<Post> findAllByParentNull();

    /**
     * find all parent posts CREATED by a user with the specified user.id
     * @param userId - user.id to search for
     * @return a List of parent posts, posted by the specified user id
     */
   //Page<Post> findAllByUser_IdAndParentNullOrderByCreatedDesc( Long userId, Pageable pageable );

    /**
     * get a page of Parent Posts (aka Threads) in descending order of creation
     * @return a Page of parent posts ordered by creation date, in descending order
     */
   Page<Post> findAllByParentNullOrderByCreatedDesc( Pageable pageable );

    /**
     * get a Page of posts, parent or child, ordered by created date
     * @param pageable used to specify the number of posts to retrieve
     * @return
     */
   Page<Post> findAllByOrderByCreatedDesc( Pageable pageable );

    /**
     * get a page of posts belonging to the specified category
     * @param pageable
     * @return a Page of posts having the specified category
     */
   Page<Post> findAllByCategory_IdAndParentNull( Long categoryId, Pageable pageable );

    /**
     * get all parent posts for the specified userId, using the pageable to control the page of results returned
     * @param userId
     * @param pageable
     * @return a lists of posts made by the specified user id to be displayed on the pageable
     */
   Page<Post> findAllByUser_IdAndParentNull( Long userId, Pageable pageable );


    /**
     * get a page of parent posts for the specified userId AND having the specified categoryId
     * @param userId
     * @param categoryId
     * @param pageable
     * @return
     */
   Page<Post> findAllByUser_IdAndCategory_IdAndParentNull( Long userId, Long categoryId, Pageable pageable );

    /**
     * Get all posts for a userName ordered by created date
     * @param name
     * @return
     */
   List<Post> findAllByUser_userNameOrderByCreatedDesc( String name );

    /**
     * searches for searchStr in the text or title of a Post. This is a brute force search using SQL LIKE operator
     * @param searchStr - the substring to search for in post.text or post.title
     * @return {@link Page} containing Posts matching the searchStr
     */
    @Query("select p from Post p where lower(p.title) like %?1% or lower(p.text) like %?1% order by p.created desc ")
   Page<Post> findByTextOrTitleIgnoreCaseContaining( String searchStr, Pageable pageable );

    /**
     *
     * @return the 10 most recent posts made
     */
   List<Post> findTop10ByOrderByCreatedDesc();
}
