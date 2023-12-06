package com.session.model;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Meeting {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "mentor_id")
    private Long mentorId;

    @Column(name = "session_date") //time is also included in date format "YYYY-MM-DDTHH-MM-SS", T seperates the date & time, clock 24 hrs
    private LocalDateTime sessionDate;


    @Column(name = "booked_at")
    private LocalDateTime bookedAt;

//    @Column(name = "status")                // Status of session- scheduled, rescheduled, cancelled, completed
//    private String status;
    
    @Column(name="frequency")				//weekly or biweekly 
    private String frequency;

	public Meeting() {
		super();
	}

	public Meeting(Long userId, Long mentorId, LocalDateTime sessionDate,
			LocalDateTime bookedAt, String frequency) {
		super();
		this.userId = userId;
		this.mentorId = mentorId;
		this.sessionDate = sessionDate;
		this.bookedAt = bookedAt;
		this.frequency = frequency;
	}

	public Long getId() {
		return id;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMentorId() {
		return mentorId;
	}

	public void setMentorId(Long mentorId) {
		this.mentorId = mentorId;
	}

	public LocalDateTime getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(LocalDateTime sessionDate) {
		this.sessionDate = sessionDate;
	}

	public LocalDateTime getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(LocalDateTime bookedAt) {
		this.bookedAt = bookedAt;
	}

//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
    
    
    
}
