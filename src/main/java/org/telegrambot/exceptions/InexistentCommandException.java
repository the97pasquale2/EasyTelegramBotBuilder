package org.telegrambot.exceptions;

import org.telegram.telegrambots.api.objects.Update;
import org.telegrambot.utils.Command;

public class InexistentCommandException extends RuntimeException {

	Command command;

	public InexistentCommandException(Update update, Command command) {
		super();
		this.command = command;
	}

	public InexistentCommandException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace, Command command) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.command = command;
	}

	public InexistentCommandException(String message, Throwable cause, Command command) {
		super(message, cause);
		this.command = command;
	}

	public InexistentCommandException(String message, Command command) {
		super(message);
		this.command = command;
	}

	public InexistentCommandException(Throwable cause, Command command) {
		super(cause);
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	
	

}
