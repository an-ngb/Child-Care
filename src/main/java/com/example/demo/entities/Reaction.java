package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reactions")
public class Reaction extends AbstractAuditing{
    @Id
    @SequenceGenerator(name = "reactionSeqGen", sequenceName = "reactionSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="reactionSeqGen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_upvote")
    private Boolean isUpvote;
}