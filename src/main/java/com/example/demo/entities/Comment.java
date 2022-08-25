package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends AbstractAuditing {

    @Id
    @SequenceGenerator(name = "commentSeqGen", sequenceName = "commentSeq", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="commentSeqGen")
    @Column(name = "comments_id")
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "total_comment_like")
    private Integer totalCommentLike;

    @Column(name = "total_comment_dislike")
    private Integer totalCommentDislike;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "posts_id", nullable = false)
    private Post post;

    public Comment(String comment, Post post){
        this.comment = comment;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
