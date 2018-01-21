package org.telegrambot.exceptions;

import java.lang.reflect.Method;

import org.telegram.telegrambots.api.objects.Update;

public class WrongParamsNumberException extends RuntimeException {

	Update update;
	String method;
	Object[] givenParams;
	
	public WrongParamsNumberException(Update update, String method, Object[] givenParams) {
		this.update = update;
		this.method = method;
		this.givenParams = givenParams;
	}

	public WrongParamsNumberException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace, Update update, String method, Object[] givenParams) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.update = update;
		this.method = method;
		this.givenParams = givenParams;
	}

	public WrongParamsNumberException(String message, Throwable cause,  Update update, String method, Object[] givenParams) {
		super(message, cause);
		this.update = update;
		this.method = method;
		this.givenParams = givenParams;
	}

	public WrongParamsNumberException(String message, String method, Update update, Object[] givenParams) {
		super(message);
		this.update = update;
		this.method = method;
		this.givenParams = givenParams;
	}

	public WrongParamsNumberException(Throwable cause, Update update, String method, Object[] givenParams) {
		super(cause);
		this.update = update;
		this.method = method;
		this.givenParams = givenParams;
	}

	public Update getUpdate() {
		return update;
	}

	public void setUpdate(Update update) {
		this.update = update;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getGivenParams() {
		return givenParams;
	}

	public void setGivenParams(Object[] givenParams) {
		this.givenParams = givenParams;
	}

	
	
}
