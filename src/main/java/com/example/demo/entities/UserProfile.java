package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @SequenceGenerator(name = "userProfileSeqGen", sequenceName = "userProfileSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="userProfileSeqGen")
    @Column(name = "user_profile_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "address")
    private String address;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_vip")
    private Boolean isVip = false;

    public UserProfile(User user, String fullName, String address, Integer age, Integer gender, String phone) {
        this.user = user;
        this.fullName = fullName;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }
}