package com.blogen.repositories;

import com.blogen.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 
 * @author Cliff
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {


    Category findByName( String name );


    //this query type should automatically wrap the name variable in "%"
    List<Category> findByNameIgnoreCaseContaining( String name );

    /**
     * get a 'pageable' worth of categories
     * @param pageable
     * @return a page of categories according to the pageable
     */
    Page<Category> findAllBy( Pageable pageable );


}
