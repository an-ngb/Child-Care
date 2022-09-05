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
@Table(name = "reports")
public class Report extends AbstractAuditing{
    @Id
    @SequenceGenerator(name = "reportSeqGen", sequenceName = "reportSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="reportSeqGen")
    @Column(name = "report_id", nullable = false)
    private Integer id;

    @Column(name = "creator")
    private Integer creator;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Type(type = "org.hibernate.type.TextType")
    private String content;
}