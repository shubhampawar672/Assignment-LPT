package com.session.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.session.dto.ApiResponse;
import com.session.dto.SessionResponseDto;
import com.session.model.Meeting;
import com.session.service.SessionService;

import jakarta.xml.bind.ValidationException;

@RestController
@RequestMapping("api/session")
public class SessionController {
	@Autowired
	private  SessionService sessionService;
	
	@PostMapping("/book")
	ResponseEntity<Object>bookSession(@RequestBody Meeting session) throws ValidationException
	{
		return sessionService.bookSession(session);
	}
	
	@DeleteMapping("/cancel/{sessionId}")
	ResponseEntity<ApiResponse>calcelSession(@PathVariable Long sessionId)
	{
		return sessionService.cancelSession(sessionId);
	}
	
	
	@PutMapping("/reschedule/{sessionId}")
	ResponseEntity<Object>rescheduleSession(@RequestBody Meeting session, @PathVariable(name="sessionId") Long sessionId)
	{
		return sessionService.rescheduleSession(session,sessionId);
	}
	
	
	@GetMapping("/**")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException() {
        return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
    }
	

}
