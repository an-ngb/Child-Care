package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "files")
public class File extends AbstractAuditing{
    @Id
    @SequenceGenerator(name = "fileSeqGen", sequenceName = "fileSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fileSeqGen")
    @Column(name = "file_id", nullable = false)
    private Integer id;

    @Column(name = "resource_link")
    private String resourceLink;
}