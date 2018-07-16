package com.blogen.repositories;

import com.blogen.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data Repository for CRUD operations on {@link Role}
 * @author Cliff
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole( String role );
}
