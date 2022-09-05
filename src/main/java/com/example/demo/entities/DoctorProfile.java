package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "doctor_profiles")
public class DoctorProfile {
    @Id
    @SequenceGenerator(name = "doctorProfileSeqGen", sequenceName = "doctorProfileSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="doctorProfileSeqGen")
    @Column(name = "doctor_profile_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "certificate")
    @Type(type = "org.hibernate.type.TextType")
    private String certificate;

    @Column(name = "degree")
    @Type(type = "org.hibernate.type.TextType")
    private String degree;

    @Column(name = "exp_year")
    @Type(type = "org.hibernate.type.TextType")
    private String expYear;

    @Column(name = "specialist")
    @Type(type = "org.hibernate.type.TextType")
    private String specialist;

    @Column(name = "working_at")
    @Type(type = "org.hibernate.type.TextType")
    private String workingAt;

    @Column(name = "private_web")
    @Type(type = "org.hibernate.type.TextType")
    private String privateWeb;

    @Column(name = "start_work_at_time")
    private LocalDate startWorkAtTime;

    @Column(name = "end_work_at_time")
    private LocalDate endWorkAtTime;

    @Column(name = "work_at")
    @Type(type = "org.hibernate.type.TextType")
    private String workAt;
}