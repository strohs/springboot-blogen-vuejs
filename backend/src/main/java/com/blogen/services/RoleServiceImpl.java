package com.blogen.services;

import com.blogen.domain.Role;
import com.blogen.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service providing CRUD operations on {@link Role}
 *
 * @author Cliff
 */
@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl( RoleRepository roleRepository ) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<?> listAll() {
        List<Role> roles = new ArrayList<>(  );
        roleRepository.findAll().forEach( roles::add );
        return roles;
    }

    @Override
    public Role getById( Long id ) {
        return roleRepository.findById( id ).get();
    }

    @Override
    public Role getByName( String roleName ) {
        return roleRepository.findByRole( roleName );
    }


    @Override
    public Role saveOrUpdate( Role role ) {
        return roleRepository.save( role );
    }

    @Override
    public void delete( Long id ) {
        roleRepository.deleteById( id );
    }
}
