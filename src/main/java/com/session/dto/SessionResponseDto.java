package com.session.dto;

public class SessionResponseDto {

    private Long sessionId;
    private String message;
    public SessionResponseDto()
    {
    	super();
    }
    public SessionResponseDto(Long sessionId,String msg) {
        this.sessionId = sessionId;
        this.message=msg;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

