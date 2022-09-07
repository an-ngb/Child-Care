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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "certificate")
    @Type(type = "org.hibernate.type.TextType")
    private String certificate;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "degree")
    @Type(type = "org.hibernate.type.TextType")
    private String degree;

    @Column(name = "exp_year")
    private String expYear;

    @Column(name = "specialist")
    @Type(type = "org.hibernate.type.TextType")
    private String specialist;

    @Column(name = "working_at")
    private String workingAt;

    @Column(name = "private_web")
    private String privateWeb;

    @Column(name = "start_work_at_time")
    private String startWorkAtTime;

    @Column(name = "end_work_at_time")
    private String endWorkAtTime;

    @Column(name = "work_at")
    private String workAt;

    public DoctorProfile(User user,String fullName, String certificate, String degree, String expYear, String specialist, String workingAt, String privateWeb, String startWorkAtTime, String endWorkAtTime, String workAt) {
        this.user = user;
        this.fullName = fullName;
        this.certificate = certificate;
        this.degree = degree;
        this.expYear = expYear;
        this.specialist = specialist;
        this.workingAt = workingAt;
        this.privateWeb = privateWeb;
        this.startWorkAtTime = startWorkAtTime;
        this.endWorkAtTime = endWorkAtTime;
        this.workAt = workAt;
    }
}