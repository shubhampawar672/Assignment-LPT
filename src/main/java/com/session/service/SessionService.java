package com.session.service;

import java.awt.print.Pageable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;

import com.session.Respository.SessionRepository;
import com.session.dto.ApiResponse;
import com.session.dto.SessionResponseDto;
import com.session.exception.CustomValidationException;
import com.session.model.Meeting;
import com.session.validation.SessionRequestionValidator;

import ch.qos.logback.core.util.Duration;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.ValidationException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class SessionService {
	
	@Autowired
	private  SessionRepository sessionRepository;
	@Autowired
	private  SessionRequestionValidator sessionRequestionValidator;
	
	
	
	//Book session
	public ResponseEntity<Object> bookSession(Meeting session) throws ValidationException {
		
		validateSessionRequest(session);  						//validate incoming data
		
		
		//find previous session of requesting user=> to check time difference between 2 sessions
		List<Meeting> prevSession=findPrevSession(session.getUserId(),session.getSessionDate());
		
		Long mentorId = session.getMentorId();
	    LocalDateTime requestedSessionDate = session.getSessionDate();

	    // Define the minimum time gap in hours
	    int minTimeGapInHours = 3;

	    // Check if there's any existing session for the mentor within the time gap
	    boolean isSessionAvailable = isSessionAvailable(mentorId, requestedSessionDate, minTimeGapInHours);
		
	    
		//if requested session time/date  is available
		if(isSessionAvailable) 
		{
			
		    // if user have some previous session
			if(!prevSession.isEmpty())
			{
				LocalDate prevDate=prevSession.get(0).getSessionDate().toLocalDate();
				LocalDate currDate=session.getSessionDate().toLocalDate();
				
				
				long days=ChronoUnit.DAYS.between(prevDate, currDate);
	
				
				String frequency=session.getFrequency();
		
				
				if((frequency.equals("weekly") && days<7) || (frequency.equals("biweekly") && days<14)) {
					
					Long daysDifference=frequency.equals("weekly")?7-days:14-days;
					
					ApiResponse errResponse=new ApiResponse("Too early!! You can book session after "+daysDifference+" days");
					
					return new ResponseEntity<>(errResponse,HttpStatus.UNPROCESSABLE_ENTITY);
					
				}
					
			}
				//User do not have any previous session - book the session
				sessionRepository.save(session);
				Meeting currentMeeting=sessionRepository.findByUserIdAndMentorIdAndSessionDateAndFrequencyAndBookedAt
														(session.getUserId(),
														session.getMentorId(),session.getSessionDate(),
														session.getFrequency(), session.getBookedAt());
				
				SessionResponseDto response=new SessionResponseDto(currentMeeting.getId(),
																  "Session Booked for "+session.getSessionDate().toLocalDate()
																  +"  "+session.getSessionDate().toLocalTime());
				return new ResponseEntity<>(response,HttpStatus.OK);
		}else {
			 
			 ApiResponse errorResponse=new ApiResponse("Session cannot be booked. Time conflict with existing session.");
			 return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
		}
		
	}
	
	
	
	//find previous session of user
	private  List<Meeting> findPrevSession(Long id,LocalDateTime currDate) {

		PageRequest  pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "sessionDate"));
		 
	        return sessionRepository.findMostRecentSessionByUserIdAndDate(id, currDate,pageable);
	                
	}
	
	
	
	//check if session is available for request sessionTime with given mentor 
	public boolean isSessionAvailable(Long mentorId, LocalDateTime requestedSessionDate, int minTimeGapInHours) {
	    // Find the most recent session for the consultant
	    Optional<Meeting> mostRecentSession = sessionRepository.findMostRecentSessionByMentorId(mentorId);

	    if (mostRecentSession.isPresent()) {
	        LocalDateTime mostRecentSessionDate = mostRecentSession.get().getSessionDate();

	        // Calculate the time gap between the most recent session and the requested session
	        long timeGapInHours = ChronoUnit.HOURS.between(mostRecentSessionDate, requestedSessionDate);

	        // Check if the time gap is greater than or equal to the minimum required gap
	        return timeGapInHours >= minTimeGapInHours;
	    }

	    // If there's no existing session, the session is available
	    return true;
	}

	
	
	
	//validate session request
	private void validateSessionRequest(Meeting sessionRequest) throws CustomValidationException {
	    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(sessionRequest, "sessionRequest");
	    sessionRequestionValidator.validate(sessionRequest, errors);

	    if (errors.hasErrors()) {
	    	StringBuilder errorMessage = new StringBuilder("Validation failed for session request : ");
	        
	        for (ObjectError error : errors.getAllErrors()) {
	            errorMessage.append(error.getDefaultMessage());
	        }

	        throw new CustomValidationException(errorMessage.toString());
	    }
	}


	
	
	//Cancel Session by userId
	@Transactional
	public ResponseEntity<ApiResponse> cancelSession(Long sessionId) {
		
		Optional<Meeting> isValidsessionId=sessionRepository.findById(sessionId);
		
		if(isValidsessionId.isEmpty())
		{
			ApiResponse errorResponse=new ApiResponse("No scheduled session for "+sessionId);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		    
		    Meeting meet=isValidsessionId.get();
			LocalDateTime sessionTime=meet.getSessionDate();
			LocalDateTime currTime= LocalDateTime.now();
		        
			Long hours = ChronoUnit.HOURS.between(currTime,sessionTime);

			if(hours<13)
			{
				ApiResponse errorResponse=new ApiResponse("Session can't be cancelled before 12 hours of commencement.");
				return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
			}else {
				sessionRepository.deleteById(sessionId);
				ApiResponse errorResponse=new ApiResponse("Session cancelled successfully.");
			    return new ResponseEntity<>(errorResponse,HttpStatus.OK);
				
			}	
		
		
	}


	//reschedule session: only before 4 hours of session commencement
	public ResponseEntity<Object> rescheduleSession(Meeting session,Long sessionId) {
		
		validateSessionRequest(session);  
		Optional<Meeting> meet=sessionRepository.findById(sessionId);
		
		//no scheduled session for given sessionId
		if(meet.isEmpty())
		{
			ApiResponse errorResponse=new ApiResponse("No scheduled session for "+sessionId);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}else {
					
			Long mentorId = session.getMentorId();
		    LocalDateTime requestedSessionDate = session.getSessionDate();

		    // Define the minimum time gap in hours
		    int minTimeGapInHours = 3;

		    // Check if there's any existing session for the mentor within the time gap
		    boolean isSessionAvailable = isSessionAvailable(mentorId, requestedSessionDate, minTimeGapInHours);
			
		    
		    //if session available for requested time
			if(isSessionAvailable) {
				
			Meeting m=meet.get();
			LocalDateTime sessionTime=m.getSessionDate();
			LocalDateTime currTime= LocalDateTime.now();
		        
			Long hours = ChronoUnit.HOURS.between(currTime,sessionTime);
			
			//if time remaining for starting session is <5hours 
			if(hours<5)
			{   ApiResponse errorResponse=new ApiResponse("Session can't be rescheduled before 4 hours of commencement.");
				return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
			}
			
				//reschedule session
				
				sessionRepository.deleteById(sessionId);
				sessionRepository.save(session);
				
				Meeting currentMeeting=sessionRepository.findByUserIdAndMentorIdAndSessionDateAndFrequencyAndBookedAt
						(session.getUserId(),
						session.getMentorId(),session.getSessionDate(),
						session.getFrequency(), session.getBookedAt());
				
				SessionResponseDto response=new SessionResponseDto(currentMeeting.getId(),"Success!! Session rescheduled on Date "
																	+session.getSessionDate().toLocalDate()+
																	" Time "+session.getSessionDate().toLocalTime());
			    return new ResponseEntity<>(response,HttpStatus.OK);
				
				
			}
			
			 ApiResponse errorResponse=new ApiResponse("Session cannot be booked. Time conflict with existing session.");
			 return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
			
		}
	}

	
	

}
