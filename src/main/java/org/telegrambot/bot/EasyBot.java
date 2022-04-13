package org.telegrambot.bot;

import org.telegram.interfaces.ErrorHandler;

public class EasyBot {
	AccessInfoBot accessInfo;
	ErrorHandler errorHandler;
	
	public AccessInfoBot getAccessInfo() {
		return accessInfo;
	}
	public void setAccessInfo(AccessInfoBot accessInfo) {
		this.accessInfo = accessInfo;
	}
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
}
