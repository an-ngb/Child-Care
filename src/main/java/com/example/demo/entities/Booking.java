package com.example.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bookings")
public class Booking extends AbstractAuditing{
    @Id
    @SequenceGenerator(name = "bookingSeqGen", sequenceName = "bookingSeq")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bookingSeqGen")
    @Column(name = "booking_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(name = "booked_at")
    private Instant bookedAt;

    @Column(name = "shiftBooked")
    private Integer shiftBooked;

    @Column(name = "content")
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Column(name = "consult_by")
    private String consult;

    @Column(name = "is_approved")
    private Boolean isApproved;

    public Booking(User user, User doctor, Instant bookedAt, String content, Integer shiftBooked, String consult) {
        this.user = user;
        this.doctor = doctor;
        this.bookedAt = bookedAt;
        this.content = content;
        this.shiftBooked = shiftBooked;
        this.consult = consult;
    }
}