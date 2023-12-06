package com.session.exception;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.validation.ObjectError;

public class CustomValidationException extends RuntimeException {
	   
	    private String msg;
		public CustomValidationException(String msg) {
			this.msg=msg;
	      
	    }

	    public String getMessage() {
	        return msg;
	    }
	}


