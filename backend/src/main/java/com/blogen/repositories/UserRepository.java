package com.blogen.repositories;

import com.blogen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring-Data-JPA Repository for CRUD ops on {@link User} data
 *
 * @author Cliff
 */
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserName( String userName );

    List<User> findByUserNameIgnoreCaseContaining( String name );
}
