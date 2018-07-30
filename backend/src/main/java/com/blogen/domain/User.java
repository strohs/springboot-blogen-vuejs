package com.blogen.domain;

import lombok.*;

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
@EqualsAndHashCode(of = {"id","userName"})
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @Column(nullable = false)
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

}
