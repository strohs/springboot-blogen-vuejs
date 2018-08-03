package com.blogen.api.v1.services;

import com.blogen.api.v1.controllers.UserController;
import com.blogen.api.v1.model.PasswordRequestDTO;
import com.blogen.api.v1.model.UserDTO;
import com.blogen.api.v1.model.UserListDTO;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;

import java.util.Optional;

/**
 * REST operations on {@link com.blogen.domain.User}
 *
 * @author Cliff
 */
public interface UserService {

    /**
     * fetch a list of all users
     * @return a DTO containing a list of {@link UserDTO}
     */
    UserListDTO getAllUsers();

    /**
     * fetch a {@link User} by their id
     * @param id database id of the user
     * @return a UserDTO
     */
    UserDTO getUser( Long id );

    /**
     * update User fields using fields in the userDTO
     * @param user exiting User to update
     * @param userDTO - DTO containing user fields to update
     * @return a UserDTO containing the User's updated information
     */
    UserDTO updateUser( User user, UserDTO userDTO );

    /**
     * fetch a {@link User} from the repository, by their userName
     * @param name - the username
     * @return an Optional containing the User
     */
    Optional<User> findByUserName( String name );

    /**
     * fetch a {@link User} from the repository by their id
     * @param id
     * @return
     */
    Optional<User> findById( Long id );

    /**
     * save a {@link User} into the repository
     * @param user the {@link User} to save
     * @return the saved {@link User}
     */
    User saveUser( User user );

    /**
     * change a user's password
     * @param user the {@link User} who will get their password changed
     * @param passwordRequestDTO DTO containing the new password
     */
    void changePassword( User user, PasswordRequestDTO passwordRequestDTO );

    /**
     * build a HATEOAS style Url that identifies a specific user
     * @param user {@link User} object that will be used to build the userURL
     * @return a relative URL to the passed in user
     */
    static String buildUserUrl( User user ) {
        return UserController.BASE_URL + "/" + user.getId();
    }

    UserPrefs buildDefaultUserPrefs();

}
