package com.blogen.api.v1.mappers;

import com.blogen.domain.User;
import com.blogen.services.security.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Map a {@link User} domain object to Spring security {@link UserDetails}
 *
 * @author Cliff
 */
@Mapper
public interface UserDetailsMapper {

    UserDetailsMapper INSTANCE = Mappers.getMapper( UserDetailsMapper.class );

    /**
     * map a {@link User} and their {@link com.blogen.domain.Role}s to Spring Security {@link UserDetails} and
     * GrantedAuthorities
     * @param user the user to map to a UserDetails object
     * @return a UserDetails containing some User properties, namely username,password,enabled and authorities
     */
    default UserDetails userToUserDetails( User user ) {
        UserDetailsImpl userDetails = new UserDetailsImpl();

        if (user != null) {
            userDetails.setUsername( user.getUserName() );
            //TODO may need to set the plain password here
            userDetails.setPassword( user.getEncryptedPassword() );
            userDetails.setEnabled( user.getEnabled() );

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            });
            userDetails.setAuthorities(authorities);
        }
        return userDetails;
    }

}
