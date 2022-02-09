package com.blogen.services.security;

import com.blogen.domain.User;
import com.blogen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of Spring UserDetailsService for our Blogen users
 *
 * @author Cliff
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {


    private UserRepository userRepository;


    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);

        if (user != null) {
            return userToUserDetails(user);
        } else {
            throw new UsernameNotFoundException("user repository returned null for username" + username);
        }
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails authenticateUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
        return userToUserDetails(user);
    }

    /**
     * maps a Blogen User domain object to a spring security UserDetails object
     *
     * @param user blogen User domain object
     * @return UserDetails
     */
    private UserDetails userToUserDetails(User user) {
        UserDetailsImpl principal = new UserDetailsImpl();

        principal.setUsername(user.getUserName());
        principal.setPassword(user.getEncryptedPassword());
        principal.setEnabled(user.getEnabled());

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles()
                .forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getRole()));
                });
        principal.setAuthorities(authorities);

        return principal;
    }
}
