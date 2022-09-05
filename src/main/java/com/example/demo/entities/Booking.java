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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @Column(name = "booked_at")
    private Instant bookedAt;

    @Column(name = "booked_time")
    private Instant bookedTime;

    @Column(name = "content")
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Column(name = "is_approved")
    private Boolean isApproved;

}