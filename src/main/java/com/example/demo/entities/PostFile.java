package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "post_files")
public class PostFile {
    @Id
    @SequenceGenerator(name = "postFileSeqGen", sequenceName = "postFileSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="postFileSeqGen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_file_id")
    private File postFile;
}