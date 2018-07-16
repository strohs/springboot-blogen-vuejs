package com.blogen.bootstrap;

import com.blogen.domain.Category;

/**
 * Builder used to bootstrap Category data
 * @author Cliff
 */
public class CategoryBuilder {

    public static Category build( String name ) {
        Category c = new Category();
        c.setName( name );
        return c;
    }
}
