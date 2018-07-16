package com.blogen.services.security;

import com.blogen.api.v1.mappers.UserDetailsMapper;
import com.blogen.domain.User;
import com.blogen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Spring UserDetailsService for our Blogen users
 * @author Cliff
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {


    private UserRepository userRepository;

    //this is used to map our Domain Object, User, into Spring Security's UserDetails interface
    private UserDetailsMapper userDetailsMapper;

    //declared as @Lazy here to prevent a circular dependency from occurring with UserService
    @Autowired
    public UserDetailsServiceImpl( UserRepository userRepository, UserDetailsMapper userDetailsMapper ) {
        this.userRepository = userRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userRepository.findByUserName( username );
        UserDetails userDetails = userDetailsMapper.userToUserDetails( user );
        return userDetails;
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails authenticateUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );
        UserDetails userDetails = userDetailsMapper.userToUserDetails( user );
        return userDetails;
    }
}
