package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "report_files")
public class ReportFile {
    @Id
    @SequenceGenerator(name = "reportFileSeqGen", sequenceName = "reportFileSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="reportFileSeqGen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_file_id")
    private File reportFile;
}