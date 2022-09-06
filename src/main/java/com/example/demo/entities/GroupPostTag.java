package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "group_post_tag")
public class GroupPostTag {
    @Id
    @SequenceGenerator(name = "groupPostTagSeqGen", sequenceName = "groupPostTagSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="groupPostTagSeqGen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_post_id")
    private GroupPost groupPost;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_tag_id")
    private GroupTag groupTag;

    public GroupPostTag(GroupPost groupPost, GroupTag groupTag) {
        this.groupPost = groupPost;
        this.groupTag = groupTag;
    }
}