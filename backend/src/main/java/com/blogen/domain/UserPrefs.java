package com.blogen.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Domain object that will hold user specific preferences for the Blogen web-site
 *
 * Author: Cliff
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class UserPrefs {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    Long id;

    @OneToOne
    User user;

    //the users Avatar
    @OneToOne(fetch = FetchType.EAGER)
    Avatar avatar;

    @Override
    public String toString() {
        return "UserPrefs{" +
                "id=" + id +
                ", userName=" + (user != null ?  user.getUserName() : "null") +
                ", avatar='" + (avatar != null ?  avatar.getFileName() : "null") +
                '}';
    }
}
