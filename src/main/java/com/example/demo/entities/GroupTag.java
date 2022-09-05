package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "group_tags")
public class GroupTag {
    @Id
    @SequenceGenerator(name = "groupTagSeqGen", sequenceName = "groupTagSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="groupTagSeqGen")
    @Column(name = "group_tag_id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;
}