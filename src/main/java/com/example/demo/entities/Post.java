package com.example.demo.entities;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends AbstractAuditing{
    @Id
    @SequenceGenerator(name = "postSeqGen", sequenceName = "postSeq", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="postSeqGen")
    @Column(name = "posts_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "total_post_like")
    private Integer totalLike;

    @Column(name = "total_post_dislike")
    private Integer totalDislike;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comment> comment = new ArrayList<>();

//    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Tag> tagsList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Post post = (Post) o;
        return id != null && Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
