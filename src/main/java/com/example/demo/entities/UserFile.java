package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_files")
public class UserFile {
    @Id
    @SequenceGenerator(name = "userFileSeqGen", sequenceName = "userFileSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="userFileSeqGen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_file_id")
    private File userFile;
}