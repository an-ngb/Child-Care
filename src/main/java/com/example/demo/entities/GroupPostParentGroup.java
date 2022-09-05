package com.example.demo.entities;

import com.example.demo.entities.GroupPost;

import javax.persistence.*;

@Entity
@Table(name = "group_post_parent_groups")
public class GroupPostParentGroup {
    @Id
    @SequenceGenerator(name = "groupPostParentGroupSeqGen", sequenceName = "groupPostParentGroupSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="groupPostParentGroupSeqGen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_post_id")
    private GroupPost groupPost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GroupPost getGroupPost() {
        return groupPost;
    }

    public void setGroupPost(GroupPost groupPost) {
        this.groupPost = groupPost;
    }

}