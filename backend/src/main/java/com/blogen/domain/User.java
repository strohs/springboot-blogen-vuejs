package com.blogen.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for a User of Blogen
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"userName"})
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @Column(nullable = false, unique = true)
    private String userName;

    @Transient
    private String password;

    private String encryptedPassword;

    private Boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    // ~ defaults to @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "user_id"),
    //     inverseJoinColumns = @joinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();


    //deleting a user should delete their userPrefs
    @OneToOne(cascade = CascadeType.ALL)
    private UserPrefs userPrefs;


    public void setUserPrefs( UserPrefs userPrefs ) {
        if ( userPrefs != null ) {
            this.userPrefs = userPrefs;
            userPrefs.setUser( this );
        }
    }

    public void addRole(Role role){
        if(!this.roles.contains(role)){
            this.roles.add(role);
        }
        if(!role.getUsers().contains(this)){
            role.getUsers().add(this);
        }
    }

    public void removeRole(Role role){
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder( "User{" );
        sb.append( "id=" ).append( id );
        sb.append( ", firstName='" ).append( firstName ).append( '\'' );
        sb.append( ", lastName='" ).append( lastName ).append( '\'' );
        sb.append( ", email='" ).append( email ).append( '\'' );
        sb.append( ", userName='" ).append( userName ).append( '\'' );
        sb.append( ", password='" ).append( password ).append( '\'' );
        if ( userPrefs != null )
          sb.append( ", userPrefs=" ).append( userPrefs );
        else
            sb.append( ", userPrefs=null" );
        sb.append( '}' );
        return sb.toString();
    }
}
