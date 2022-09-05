package com.example.demo.entities;

import javax.persistence.*;

@Entity
@Table(name = "parent_group")
public class ParentGroup {
    @Id
    @SequenceGenerator(name = "parentGroupSeqGen", sequenceName = "parentGroupSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="parentGroupSeqGen")
    @Column(name = "parent_group_id", nullable = false)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //TODO [JPA Buddy] generate columns from DB
}