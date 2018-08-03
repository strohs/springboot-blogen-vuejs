package com.blogen.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Domain Model for the Category of a Post
 * @author Cliff
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( exclude = "id")
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private LocalDateTime created = LocalDateTime.now();

}
