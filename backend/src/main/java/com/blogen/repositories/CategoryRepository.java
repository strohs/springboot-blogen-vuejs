package com.blogen.repositories;

import com.blogen.domain.Category;
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

    
}
