package com.example.demo.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(name = "userSeqGen", sequenceName = "userSeq", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="userSeqGen")
    @Column(name = "users_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "disable")
    private Boolean disable;

    @Column(name = "total_like")
    private Integer totalLike;

    @Column(name = "total_dislike")
    private Integer totalDislike;

    @Column(name = "total_comment")
    private Integer totalComment;

    @Column(name = "total_post")
    private Integer totalPost;

    @Column(name = "trust")
    private Double trust;

    @ManyToOne()
    @JoinColumn(name = "role_id", nullable = false)
    @ToString.Exclude
    private Role role;

    @Column(name = "token")
    private String token;

    public User(String email, String password, String fullName, Integer age, Integer gender, String phone, Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.role = role;
        this.setDisable(false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
