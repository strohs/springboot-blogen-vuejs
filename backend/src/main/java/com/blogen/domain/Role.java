package com.blogen.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain object containing the roles a User of blogen may have.
 * Primarily used for/by Spring Security
 *
 * @author Cliff
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String role;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        if(!this.users.contains(user)){
            this.users.add(user);
        }

        if(!user.getRoles().contains(this)){
            user.getRoles().add(this);
        }
    }

    public void removeUser(User user){
        this.users.remove(user);
        user.getRoles().remove(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder( "Role{" );
        sb.append( "id=" ).append( id );
        sb.append( ", role='" ).append( role ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}
