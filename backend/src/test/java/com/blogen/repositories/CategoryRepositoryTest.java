package com.blogen.repositories;

import com.blogen.domain.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


/**
 * Test CategoryRepository, this will use the H2 schema(schema-h2.sql) and data files(data-h2.sql) from the
 * resources directory
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryTest {

    // you can inject a TestEntityManager if needed
//    @Autowired
//    private TestEntityManager entityManager;
//

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void should_find_a_category_by_name() {
        Category business = categoryRepository.findByName("Business");
        assertThat( business, is( notNullValue() ));
    }

    @Test
    public void should_return_a_page_of_three_categories() {
        // given a page request for 3 categories
        Pageable pageable = PageRequest.of(0, 3);

        Page<Category> categoryPage = categoryRepository.findAllBy( pageable );

        assertThat( categoryPage.getNumberOfElements(), is(3) );
    }
}
