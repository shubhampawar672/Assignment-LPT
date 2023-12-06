package com.session.Respository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.session.model.Meeting;

@Repository
public interface SessionRepository extends JpaRepository<Meeting, Long>{

	@Query("SELECT m FROM Meeting m WHERE m.userId = :userId AND m.sessionDate <= :sessionDate ORDER BY m.sessionDate DESC")
    List<Meeting> findMostRecentSessionByUserIdAndDate(@Param("userId") Long userId,
    													@Param("sessionDate") LocalDateTime sessionDate,PageRequest  pageable);

	 Meeting findByUserIdAndMentorIdAndSessionDateAndFrequencyAndBookedAt(
		        Long userId,
		        Long mentorId,
		        LocalDateTime sessionDate,
		        String frequency,
		        LocalDateTime bookedAt
		    );

	void deleteByUserIdAndMentorIdAndSessionDateAndFrequencyAndBookedAt(Long userId, Long mentorId,
			LocalDateTime sessionDate, String frequency, LocalDateTime bookedAt);

	Optional<Meeting> findMostRecentSessionByMentorId(Long consultantId);





}

