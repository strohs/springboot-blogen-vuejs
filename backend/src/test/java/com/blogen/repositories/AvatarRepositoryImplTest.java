package com.blogen.repositories;

import com.blogen.domain.Avatar;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A test case for testing JDBC functionality, this is not really used in this application, as we are
 * mainly using JPA.
 * <p>
 * <p>
 * Author: Cliff
 */
@JdbcTest
@ActiveProfiles({"jdbc"})
@ComponentScan({"com.blogen.repositories"})
@Disabled
public class AvatarRepositoryImplTest {

    @Autowired
    private AvatarRepository avatarRepository;


    @Test
    public void findById() {
        Optional<Avatar> av = avatarRepository.findById(1L);
        assertThat(av.isPresent(), is(true));
        assertThat(av.get().getFileName(), is("avatar0.jpg"));
    }
}