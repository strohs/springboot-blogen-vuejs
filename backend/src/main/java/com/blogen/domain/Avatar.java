package com.blogen.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Author: Cliff
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avatar {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(unique = true, updatable = false)
    private String fileName;

}
