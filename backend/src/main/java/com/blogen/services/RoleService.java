package com.blogen.services;

import com.blogen.domain.Role;

import java.util.List;

/**
 * @author Cliff
 */
public interface RoleService {

    List<?> listAll();

    Role getById(Long id);

    Role getByName(String role);

    Role saveOrUpdate(Role role);

    void delete(Long id);
}
