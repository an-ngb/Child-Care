package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "parent_group")
public class ParentGroup {
    @Id
    @SequenceGenerator(name = "parentGroupSeqGen", sequenceName = "parentGroupSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="parentGroupSeqGen")
    @Column(name = "parent_group_id", nullable = false)
    private Integer id;

    @Column(name = "parent_group_name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //TODO [JPA Buddy] generate columns from DB

    public ParentGroup(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}