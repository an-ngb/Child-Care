package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
public class Post extends AbstractAuditing{
    @Id
    @SequenceGenerator(name = "postSeqGen", sequenceName = "postSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="postSeqGen")
    @Column(name = "post_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_post_id")
    private GroupPost groupPost;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "content")
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    public Post(GroupPost groupPost) {
        this.groupPost = groupPost;
    }
}