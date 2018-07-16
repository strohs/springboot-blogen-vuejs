package com.blogen.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Model for a blogen Post. A Post in Blogen will either be a "Parent" post or a "child" post. A parent post can have
 * any number of child posts, but we only go one level deep. That is to say, you can reply to a Parent post, but you
 * cannot reply to a child post. This is by design to keep the model as simple as possible.
 *
 * @author Cliff
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode( of = "uuid" )
@Entity
public class Post {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String title;

    private String text;

    private String imageUrl;

    //a quick and hacky way to generate a unique id for a post
    private String uuid = UUID.randomUUID().toString();

    //the user who created this post
    @ManyToOne
    private User user;

    @OneToOne
    private Category category;

    private LocalDateTime created = LocalDateTime.now();

    //this is the "one" side of the relationship
    @ManyToOne( cascade = CascadeType.ALL )
    private Post parent;

    //many side is the, "child side" and owner of the relationship, Usually this is the side with the foreign key.
    @OneToMany( mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Post> children = new ArrayList<>();


    public Post addChild( Post child ) {
        child.setParent( this );
        children.add( child );
        return child;
    }

    public void removeChild( Post child ) {
        //for Collections.remove() to work, need to make sure the post.equals() and hashCode() methods are working correctly
        boolean isRemoved = children.remove( child );
        if ( isRemoved ) child.setParent( null );
    }

    /**
     * A blogen post is either a parent post or a child of a parent post.
     * A parent post will have a parent_id == null;
     *
     * @return true if post is a parent post
     */
    public boolean isParentPost() {
        return this.parent == null;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", title=" + title +
                ", text='" + text + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userName=" + user.getUserName() +
                ", category=" + category.getName() +
                ", created=" + created +
                ", childPostCount=" + children.size() +
                '}';
    }

    //The @OneToMany association is by definition a parent association, even if it’s a unidirectional or a
    // bidirectional one. Only the parent side of an association makes sense to cascade its entity state transitions to children.
    // ex. @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    //     private List<Phone> phones = new ArrayList<>();
    //
    //
    //On the other hand, a bidirectional @OneToMany association is much more efficient because the child entity controls the association.
}
