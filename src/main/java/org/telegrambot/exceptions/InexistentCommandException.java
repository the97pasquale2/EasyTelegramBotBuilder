package org.telegrambot.exceptions;

import org.telegram.telegrambots.api.objects.Update;
import org.telegrambot.utils.Command;

public class InexistentCommandException extends RuntimeException {

	Update update;
	Command command;

	public InexistentCommandException(Update update, Command command) {
		super();
		this.update = update;
		this.command = command;
	}

	public InexistentCommandException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace, Update update, Command command) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.update = update;
		this.command = command;
	}

	public InexistentCommandException(String message, Throwable cause, Update update, Command command) {
		super(message, cause);
		this.update = update;
		this.command = command;
	}

	public InexistentCommandException(String message, Update update, Command command) {
		super(message);
		this.update = update;
		this.command = command;
	}

	public InexistentCommandException(Throwable cause, Update update, Command command) {
		super(cause);
		this.update = update;
		this.command = command;
	}

	public Update getUpdate() {
		return update;
	}

	public void setUpdate(Update update) {
		this.update = update;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
	
	

}
