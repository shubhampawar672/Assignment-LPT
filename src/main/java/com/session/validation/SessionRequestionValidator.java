package com.session.validation;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.session.model.Meeting;

@Component
public class SessionRequestionValidator implements Validator  {

    @Override
    public boolean supports(Class<?> clazz) {
        return Meeting.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Meeting sessionRequest = (Meeting) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "field.required", "User ID is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mentorId", "field.required", "Mentor ID is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sessionDate", "field.required", "sessionDate is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bookedAt", "field.required", "bookedAt is required");
        
        if (sessionRequest.getFrequency() == null ||
                (!sessionRequest.getFrequency().equals("weekly") && !sessionRequest.getFrequency().equals("biweekly"))){
            errors.rejectValue("Frequency", "field.invalid", "Invalid frequency value. Must be either 'weekly' or 'biweekly'");
        }
       
        validateFutureDateTime(sessionRequest.getSessionDate(), errors);      // Check if the  date and time is in the past
       
      
    }
    
     // Check if the  date and time is in the past
    private void validateFutureDateTime(LocalDateTime sessionDate, Errors errors) {
        LocalDateTime currentDateTime = LocalDateTime.now(); 
        if (sessionDate.isBefore(currentDateTime)) {
            errors.rejectValue("sessionDate", "invalid.sessionDate", "session date and time must not be in the past");
        }
    }
    
   

}
